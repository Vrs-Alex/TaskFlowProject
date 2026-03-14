package vrsalex.api.dto.workspace.project

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import vrsalex.api.dto.common.DtoOptionalField
import vrsalex.api.dto.common.SyncUpdateEntity
import kotlin.uuid.Uuid

@Serializable
data class ProjectUpdateRequest(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,
    val areaId: DtoOptionalField<Long?> = DtoOptionalField.Undefined,
    val statusId: DtoOptionalField<Int> = DtoOptionalField.Undefined,
    val name: DtoOptionalField<String> = DtoOptionalField.Undefined,
    val color: DtoOptionalField<String> = DtoOptionalField.Undefined,
    val description: DtoOptionalField<String?> = DtoOptionalField.Undefined,
    val dueDate: DtoOptionalField<LocalDate?> = DtoOptionalField.Undefined
): SyncUpdateEntity {
    init {
        require(
            (areaId != DtoOptionalField.Undefined || statusId != DtoOptionalField.Undefined
                    || name != DtoOptionalField.Undefined || color != DtoOptionalField.Undefined
                    || description != DtoOptionalField.Undefined || dueDate != DtoOptionalField.Undefined)
        ){
            "Вы не обновили ни одного поля"
        }
    }
}
