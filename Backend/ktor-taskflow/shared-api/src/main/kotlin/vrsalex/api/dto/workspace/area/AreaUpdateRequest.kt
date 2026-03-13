package vrsalex.api.dto.workspace.area

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class AreaUpdateRequest(
    val id: Long,
    val clientId: Uuid,
    val name: String? = null,
    val color: String? = null,
    val version: Int
){
    init {
        require(name != null || color != null){ "Вы ничего не изменили" }
    }
}