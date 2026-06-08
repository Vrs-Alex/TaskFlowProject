package vrsalex.core.sync

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import vrsalex.core.exception.AppException
import vrsalex.core.security.user.UserPrincipal
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.toModelDto
import vrsalex.core.sync.service.SyncService
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

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
 * @param TCreateReq Класс входящего JSON-запроса на создание.
 * @param TUpdateReq Класс входящего JSON-запроса на обновление.
 * @param TCreateModel Внутренняя модель данных для создания.
 * @param TUpdateModel Внутренняя модель данных для обновления.
 *
 * @param path Базовый сегмент пути (например, "area").
 * @param service Реализация [vrsalex.core.sync.service.SyncService] для обработки логики.
 * @param toCreateDomain Маппер из тела запроса во внутреннюю DTO создания.
 * @param toUpdateDomain Маппер из тела запроса во внутреннюю DTO обновления.
 * @param toResponseDto Маппер из доменной модели в объект ответа (JSON).
 *
 * @throws [[AppException.Conflict]] (409) Если при обновлении или удалении версии не совпали.
 * @throws [[AppException.BadRequest]] (400) При неверных параметрах ID или версии.
 */
inline fun <reified T : SyncModel, TCreateModel : SyncClientId, TUpdateModel : SyncClientId,
        reified TCreateReq : Any, reified TUpdateReq : Any, reified TResponse: SyncDto> Route.syncRoute(
    path: String,
    service: SyncService<T, TCreateModel, TUpdateModel>,
    crossinline toCreateDomain: (TCreateReq) -> TCreateModel,
    crossinline toUpdateDomain: (TUpdateReq) -> TUpdateModel,
    crossinline toResponseDto: (T) -> TResponse
) {
    route(path) {

        get("/sync") {
            val principal = call.principal<UserPrincipal>()!!
            val lastSync = call.request.queryParameters["lastSync"]?.let { Instant.parse(it) }

            val changes = service.getChanges(lastSync, principal.internalId)
                .map { model -> model.toModelDto { toResponseDto(it)} }

            call.respond(changes)
        }

        get("/sync/{id}"){
            val principal = call.principal<UserPrincipal>()!!
            val id = Uuid.parseOrNull(call.parameters["id"] ?: "") ?: throw AppException.BadRequest("Неверный ID")
            val changes = service.findByClientId(id, principal.internalId)
            call.respond(changes.toModelDto { toResponseDto(it)} )
        }

        get("/{id}") {
            val principal = call.principal<UserPrincipal>()!!
            val id = Uuid.parseOrNull(call.parameters["id"] ?: "") ?: throw AppException.BadRequest("Неверный ID")
            val res = service.findByClientId(id, principal.internalId)
            if (res.isDeleted) call.respond(HttpStatusCode.NotFound)
            else call.respond(toResponseDto(res))
        }

        post {
            val principal = call.principal<UserPrincipal>()!!
            val request = call.receive<TCreateReq>()
            val deviceId = call.request.headers["X-Device-Id"] ?: ""
            val newObject = service.create(toCreateDomain(request), principal.internalId, deviceId)
            call.respond(HttpStatusCode.Created, toResponseDto(newObject))
        }

        patch {
            val principal = call.principal<UserPrincipal>()!!
            val request = call.receive<TUpdateReq>()
            val deviceId = call.request.headers["X-Device-Id"] ?: ""

            val newObject = service.update(toUpdateDomain(request), principal.internalId, deviceId)
            call.respond(HttpStatusCode.OK, toResponseDto(newObject))
        }

        delete("/{clientId}") {
            val principal = call.principal<UserPrincipal>()!!
            val clientId = Uuid.parseOrNull(call.parameters["clientId"] ?: "")
                ?: throw AppException.BadRequest("Неверный ID")
            val id = call.queryParameters["id"]?.toLongOrNull()
                ?: throw AppException.BadRequest("Неверный ID")
            val version = call.request.queryParameters["version"]?.toIntOrNull()
                ?: throw AppException.BadRequest("Для удаления обязательно нужно указать текущую версию сущности")
            val deviceId = call.request.headers["X-Device-Id"] ?: ""
            service.delete(id, clientId, version, principal.internalId, deviceId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}