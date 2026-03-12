package vrsalex.feature.workspace.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.sync.BaseSyncService
import vrsalex.core.sync.SyncService
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate
import vrsalex.feature.workspace.domain.repository.TagRepository

class TagService(
    private val tagRepository: TagRepository,
    private val transactionManager: TransactionManager
): BaseSyncService<Tag, TagCreate, TagUpdate>(tagRepository, transactionManager)