package vrsalex.core.model

import kotlinx.serialization.Serializable
import vrsalex.shared.api.realtime.EntityTypeDto

@Serializable
enum class EntityType {

    TAG, AREA,
    NOTE, EVENT, TASK, TASK_LOG, HABIT, GOAL,
    REMINDER, ATTACHMENT

}


fun EntityType.toDto() = EntityTypeDto.valueOf(this.name)
fun EntityTypeDto.toEntity() = EntityType.valueOf(this.name)