package vrsalex.area.domain

import vrsalex.core.sync.repository.SyncRepository

interface AreaRepository: SyncRepository<Area, AreaCreate, AreaUpdate>