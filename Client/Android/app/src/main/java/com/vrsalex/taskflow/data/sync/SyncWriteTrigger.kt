package com.vrsalex.taskflow.data.sync

import androidx.room.InvalidationTracker
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.domain.sync.service.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Мгновенный PUSH. Подписывается на изменения локальных таблиц через Room [InvalidationTracker]
 * и после любой локальной записи (с небольшим дебаунсом) отправляет накопленные изменения на сервер.
 *
 * - Дебаунс схлопывает всплеск правок в один проход.
 * - [SyncService.pushAll] идёт по тирам приоритета, поэтому порядок FK сохраняется.
 * - Записи от PULL (`upsertFromRemote`) тоже триггерят проход, но он будет no-op:
 *   `getDirty()` вернёт пусто, так как такие записи уже `isSynced = true`. Зацикливания нет.
 *
 * Живёт всё время жизни приложения. [start] вызывается один раз при старте.
 */
class SyncWriteTrigger(
    private val db: AppDatabase,
    private val syncService: SyncService,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var job: Job? = null

    private val observer = object : InvalidationTracker.Observer(WATCHED_TABLES) {
        override fun onInvalidated(tables: Set<String>) = schedule()
    }

    fun start() = db.invalidationTracker.addObserver(observer)

    private fun schedule() {
        job?.cancel()
        job = scope.launch {
            delay(DEBOUNCE_MS)
            syncService.pushAll()
        }
    }

    private companion object {
        const val DEBOUNCE_MS = 300L

        // sync_cursor намеренно НЕ слушаем: его пишет PULL, незачем триггерить push.
        val WATCHED_TABLES = arrayOf("area", "tag", "note", "note_tag", "task", "event", "task_log")
    }
}
