package vrsalex.feature.workspace.domain.repository

import vrsalex.core.sync.SyncRepository
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate

interface TagRepository: SyncRepository<Tag, TagCreate, TagUpdate>