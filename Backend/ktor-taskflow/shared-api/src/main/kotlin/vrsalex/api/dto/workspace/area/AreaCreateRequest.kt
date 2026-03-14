package vrsalex.api.dto.workspace.area

import kotlinx.serialization.Serializable
import vrsalex.api.dto.common.SyncCreateEntity
import kotlin.uuid.Uuid

@Serializable
data class AreaCreateRequest(
    override val clientId: Uuid,
    val name: String,
    val color: String
): SyncCreateEntity
