package vrsalex.feature.workspace.domain.repository

import vrsalex.core.sync.SyncRepository
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate

interface AreaRepository : SyncRepository<Area, AreaCreate, AreaUpdate>