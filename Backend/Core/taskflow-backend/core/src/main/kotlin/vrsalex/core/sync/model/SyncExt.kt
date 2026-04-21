package vrsalex.core.sync.model

import vrsalex.shared.api.common.ModelDto


fun <T : SyncModel, D> T.toModelDto(mapper: (T) -> D): ModelDto<D> =
    if (isDeleted) {
        ModelDto.Deleted(id, clientId, version)
    } else {
        ModelDto.Active(mapper(this))
    }
