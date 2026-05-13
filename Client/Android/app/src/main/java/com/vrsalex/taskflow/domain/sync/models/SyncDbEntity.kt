package com.vrsalex.taskflow.domain.sync.models

enum class SyncDbEntity(val priority: Int) {

    AREA(1), TAG(2),
    NOTE (3), TASK(3), EVENT(3), GOAL(3), HABIT(3),
    TASK_LOG(4), REMINDER(5), ATTACHMENT(5)

}
