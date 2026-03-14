package vrsalex.feature.workspace.domain.model

import kotlinx.datetime.LocalDate
import vrsalex.core.model.ModelOptionalField
import vrsalex.core.sync.SyncClientId
import vrsalex.core.sync.SyncModel
import vrsalex.core.sync.SyncUpdateModel
import vrsalex.core.value_object.Color
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Project(
    override val ownerId: Long,
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val isDeleted: Boolean,

    val areaId: Long?,
    val statusId: Int,
    val name: String,
    val color: Color,
    val description: String? = null,
    val dueDate: LocalDate?,
    val createdAt: Instant,
): SyncModel

data class ProjectCreate(
    override val clientId: Uuid,
    val areaId: Long?,
    val statusId: Int,
    val name: String,
    val color: Color,
    val description: String?,
    val dueDate: LocalDate?
): SyncClientId

data class ProjectUpdate(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,
    val areaId: ModelOptionalField<Long?> = ModelOptionalField.Undefined,
    val statusId: ModelOptionalField<Int> = ModelOptionalField.Undefined,
    val name: ModelOptionalField<String> = ModelOptionalField.Undefined,
    val color: ModelOptionalField<Color> = ModelOptionalField.Undefined,
    val description: ModelOptionalField<String?> = ModelOptionalField.Undefined,
    val dueDate: ModelOptionalField<LocalDate?> = ModelOptionalField.Undefined
): SyncClientId, SyncUpdateModel