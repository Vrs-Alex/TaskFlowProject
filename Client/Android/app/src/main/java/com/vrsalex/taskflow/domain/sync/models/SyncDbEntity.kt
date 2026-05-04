package com.vrsalex.taskflow.domain.sync.models

enum class SyncDbEntity(val priority: Int) {

    AREA(1), TAG(2),
    TASK(3), EVENT(3), GOAL(3), HABIT(3),
    REMINDER(4), ATTACHMENT(4), RECURRENCE(4)

}
