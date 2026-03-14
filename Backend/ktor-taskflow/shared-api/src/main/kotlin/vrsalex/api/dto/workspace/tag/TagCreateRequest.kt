package vrsalex.api.dto.workspace.tag

import kotlinx.serialization.Serializable
import vrsalex.api.dto.common.SyncCreateEntity
import kotlin.uuid.Uuid

@Serializable
data class TagCreateRequest(
    override val clientId: Uuid,
    val name: String,
    val color: String
): SyncCreateEntity
