package dto.workspace.tag

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class TagUpdateRequest(
    val id: Long,
    val clientId: Uuid,
    val version: Int,
    val name: String? = null,
    val color: String? = null
){
    init {
        require(
            (name != null || color != null)
        ){
            "name or color can't be null"
        }
    }
}
