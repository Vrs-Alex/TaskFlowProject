package vrsalex.core.sync

import vrsalex.shared.api.common.ModelDto


fun <T : SyncModel, D> T.toSyncDto(mapper: (T) -> D): ModelDto<D> =
    if (isDeleted) {
        ModelDto.Deleted(id, clientId, version)
    } else {
        ModelDto.Active(mapper(this))
    }
