package com.vrsalex.taskflow.domain.workspace.tag

import com.vrsalex.taskflow.domain.sync.repository.SyncRepository

interface TagRepository  : SyncRepository<Tag, TagCreate, TagUpdate>