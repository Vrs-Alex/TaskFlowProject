package vrsalex.core.model

import vrsalex.shared.api.realtime.EntityTypeDto

enum class EntityType {

    TAG, AREA,
    EVENT, TASK, HABIT, GOAL,
    REMINDER, ATTACHMENT, RECURRENCE

}


fun EntityType.toDto() = EntityTypeDto.valueOf(this.name)
fun EntityTypeDto.toEntity() = EntityType.valueOf(this.name)