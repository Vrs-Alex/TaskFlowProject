package com.vrsalex.taskflow.domain.workspace.tag

import com.vrsalex.taskflow.domain.sync.SyncRepository

interface TagRepository  : SyncRepository<Tag, TagCreate, TagUpdate>