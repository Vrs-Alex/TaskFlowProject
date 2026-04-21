package vrsalex.event.domain

import vrsalex.item.domain.repository.SubItemRepository

interface EventRepository: SubItemRepository<Event, EventCreate, EventUpdate>