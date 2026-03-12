package dto.workspace.tag

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class TagCreateRequest(
    val clientId: Uuid,
    val name: String,
    val color: String
)
