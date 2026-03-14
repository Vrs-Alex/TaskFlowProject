package vrsalex.api.dto.workspace.tag

import kotlinx.serialization.Serializable
import vrsalex.api.dto.common.DtoOptionalField
import vrsalex.api.dto.common.SyncUpdateEntity
import kotlin.uuid.Uuid

@Serializable
data class TagUpdateRequest(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    val name: DtoOptionalField<String> = DtoOptionalField.Undefined,
    val color: DtoOptionalField<String> = DtoOptionalField.Undefined
): SyncUpdateEntity {
    init {
        require(
            (name != DtoOptionalField.Undefined || color != DtoOptionalField.Undefined)
        ){
            "Вы не обновили ни одного поля"
        }
    }
}
