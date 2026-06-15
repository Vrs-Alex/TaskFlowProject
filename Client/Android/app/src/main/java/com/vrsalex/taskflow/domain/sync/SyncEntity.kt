package com.vrsalex.taskflow.domain.sync

enum class SyncEntity(val priority: Int) {
    AREA(0),
    TAG(0),
    NOTE(1),
    TASK(1),
    EVENT(1),
    TASK_LOG(2),
}