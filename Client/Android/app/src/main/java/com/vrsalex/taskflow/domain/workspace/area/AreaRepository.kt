package com.vrsalex.taskflow.domain.workspace.area

import com.vrsalex.taskflow.domain.sync.repository.SyncRepository

interface AreaRepository : SyncRepository<Area, AreaCreate, AreaUpdate>