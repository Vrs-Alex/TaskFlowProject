package vrsalex.core.sync

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import vrsalex.core.exception.AppException
import vrsalex.core.security.UserPrincipal
import kotlin.coroutines.Continuation
import kotlin.time.Instant

/**
 * Регистрирует набор стандартных CRUD-эндпоинтов для синхронизируемой сущности.
 *
 * Создает следующие пути относительно [path]:
 * - `GET /sync` — получение изменений (принимает query `lastSync`).
 * - `POST /` — создание новой записи (идемпотентно по `clientId`).
 * - `PUT /` — обновление существующей записи (Optimistic Lock).
 * - `DELETE /{id}` — мягкое удаление (требует query `version`).
 *
 * @param T Основная доменная модель.
 * @param CreateReq Класс входящего JSON-запроса на создание.
 * @param UpdateReq Класс входящего JSON-запроса на обновление.
 * @param CreateDto Внутренняя модель данных для создания.
 * @param UpdateDto Внутренняя модель данных для обновления.
 *
 * @param path Базовый сегмент пути (например, "area").
 * @param service Реализация [SyncService] для обработки логики.
 * @param toCreateDomain Маппер из тела запроса во внутреннюю DTO создания.
 * @param toUpdateDomain Маппер из тела запроса во внутреннюю DTO обновления.
 * @param toResponseDto Маппер из доменной модели в объект ответа (JSON).
 *
 * @throws vrsalex.exception.AppException.Conflict (409) Если при обновлении или удалении версии не совпали.
 * @throws vrsalex.exception.AppException.BadRequest (400) При неверных параметрах ID или версии.
 */
inline fun <reified T : SyncModel, reified CreateReq : Any, reified UpdateReq : Any,
        CreateDto : SyncClientId, UpdateDto : SyncClientId> Route.syncRoute(
    path: String,
    service: SyncService<T, CreateDto, UpdateDto>,
    crossinline toCreateDomain: (CreateReq) -> CreateDto,
    crossinline toUpdateDomain: (UpdateReq) -> UpdateDto,
    crossinline toResponseDto: (T) -> Any
) {
    route(path) {

        get("/sync") {
            val principal = call.principal<UserPrincipal>()!!
            val lastSync = call.request.queryParameters["lastSync"]?.let { Instant.parse(it) }

            val changes = service.getChanges(principal.internalId, lastSync).map { toResponseDto(it) }
            call.respond(changes)
        }

        post {
            val principal = call.principal<UserPrincipal>()!!
            val request = call.receive<CreateReq>()

            val id = service.create(toCreateDomain(request), principal.internalId)
            call.respond(HttpStatusCode.Created, id)
        }

        put {
            val principal = call.principal<UserPrincipal>()!!
            val request = call.receive<UpdateReq>()

            service.update(toUpdateDomain(request), principal.internalId)
            call.respond(HttpStatusCode.NoContent)
        }

        delete("/{id}") {
            val principal = call.principal<UserPrincipal>()!!
            val id = call.parameters["id"]?.toLongOrNull() ?: throw AppException.BadRequest("Неверный ID")
            val version = call.request.queryParameters["version"]?.toIntOrNull()
                ?: throw AppException.BadRequest("Для удаления обязательно нужно указать текущую версию сущности")

            service.delete(id, principal.internalId, version)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}