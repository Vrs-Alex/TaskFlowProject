package dto.workspace.area

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class AreaCreateRequest(
    val clientId: Uuid,
    val name: String,
    val color: String
)
