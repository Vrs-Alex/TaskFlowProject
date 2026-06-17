package com.vrsalex.taskflow.data.sync

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.taskflow.data.local.db.entity.SyncColumns
import com.vrsalex.taskflow.domain.common.model.Resource
import vrsalex.shared.api.common.SyncDto
import kotlin.uuid.Uuid

/**
 * Отправляет несинхронизированные (`isSynced = false`) локальные изменения на сервер.
 *
 * Действие по каждой записи выбирается по её [SyncColumns]:
 *  - `serverId == null && isDeleted` — создана и тут же удалена локально, сервер о ней не знает → [hardDelete];
 *  - `serverId == null`              — новая локальная запись → CREATE;
 *  - `isDeleted`                     — известна серверу и помечена на удаление → DELETE;
 *  - иначе                           — известна серверу и изменена → UPDATE.
 *
 * Реакция на ответ сервера (см. backend `BaseSyncService` / `syncRoute`):
 *  - 409 Conflict — на сервере версия новее → тянем актуальные данные ([pullItem]); побеждает сервер (LWW);
 *  - 410 Gone     — сущность удалена на сервере безвозвратно (с тем же `clientId` не воссоздать) → [hardDelete];
 *  - 404 NotFound — при DELETE сервер уже не знает о записи → [hardDelete] (состояния сошлись);
 *  - NoInternet   — соединения нет → прерываем проход, вернётся [Resource.Error.NoInternet];
 *  - прочее (5xx и т.п.) — запись пропускаем, она останется dirty до следующей попытки.
 *
 * Записи обрабатываются в порядке, в котором их вернул [getDirty]; за порядок между разными
 * сущностями отвечает вызывающая сторона (см. [com.vrsalex.taskflow.domain.sync.model.SyncEntity.priority]).
 */
class SyncPusher {

    suspend fun <E, D : SyncDto> push(
        getDirty: suspend () -> List<E>,
        getId: (E) -> Uuid,
        getSync: (E) -> SyncColumns,
        create: suspend (E) -> NetworkResult<D>,
        update: suspend (E) -> NetworkResult<D>,
        delete: suspend (id: Uuid, serverId: Long, version: Int) -> NetworkResult<Unit>,
        upsertFromRemote: suspend (D) -> Unit,
        hardDelete: suspend (id: Uuid) -> Unit,
        pullItem: suspend (id: Uuid) -> Unit,
    ): Resource<Unit> {
        var hadFailure = false

        for (item in getDirty()) {
            val id = getId(item)
            val sync = getSync(item)
            val serverId = sync.serverId

            val step = when {
                serverId == null && sync.isDeleted -> {
                    // Создали и удалили локально, не успев отправить — сервер о записи не знает.
                    hardDelete(id)
                    Step.OK
                }

                serverId == null -> create(item).resolve(
                    onSuccess = { upsertFromRemote(it) },
                    onGone = { hardDelete(id) }, // clientId уже удалён на сервере — воссоздать нельзя
                )

                sync.isDeleted -> delete(id, serverId, sync.version).resolve(
                    onSuccess = { hardDelete(id) },
                    onGone = { hardDelete(id) },
                    onNotFound = { hardDelete(id) },
                    onConflict = { pullItem(id) }, // на сервере новее — подтянуть, запись «воскреснет»
                )

                else -> update(item).resolve(
                    onSuccess = { upsertFromRemote(it) },
                    onConflict = { pullItem(id) }, // версия устарела — побеждает сервер (LWW)
                    onGone = { hardDelete(id) },    // удалена на сервере — сходимся к удалению
                )
            }

            when (step) {
                Step.OK -> Unit
                Step.SKIP -> hadFailure = true
                Step.OFFLINE -> return Resource.Error.NoInternet
            }
        }

        return if (hadFailure) Resource.Error.ServerError else Resource.Success(Unit)
    }

    /**
     * Сводит [NetworkResult] к [Step], вызывая обработчик только для тех кодов, которые
     * имеют смысл для конкретной операции. Необработанный код → [Step.SKIP] (запись останется dirty).
     */
    private suspend fun <D> NetworkResult<D>.resolve(
        onSuccess: suspend (D) -> Unit,
        onConflict: (suspend () -> Unit)? = null,
        onGone: (suspend () -> Unit)? = null,
        onNotFound: (suspend () -> Unit)? = null,
    ): Step = when (this) {
        is NetworkResult.Success -> {
            onSuccess(data)
            Step.OK
        }

        is NetworkResult.Error.HttpError -> when (code) {
            CONFLICT -> onConflict.runOrSkip()
            GONE -> onGone.runOrSkip()
            NOT_FOUND -> onNotFound.runOrSkip()
            else -> Step.SKIP
        }

        NetworkResult.Error.NetworkError -> Step.OFFLINE
        NetworkResult.Error.UnknownError -> Step.SKIP
    }

    private suspend fun (suspend () -> Unit)?.runOrSkip(): Step =
        if (this == null) Step.SKIP else {
            invoke()
            Step.OK
        }

    private enum class Step { OK, SKIP, OFFLINE }

    private companion object {
        const val CONFLICT = 409
        const val GONE = 410
        const val NOT_FOUND = 404
    }
}
