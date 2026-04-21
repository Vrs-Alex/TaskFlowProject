package vrsalex.event.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.sync.service.BaseSyncService


class EventService(
    repository: EventRepository,
    transactionManager: TransactionManager,
) : BaseSyncService<Event, EventCreate, EventUpdate, EventRepository>(repository, transactionManager)