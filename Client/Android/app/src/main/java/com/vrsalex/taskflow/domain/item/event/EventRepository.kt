package com.vrsalex.taskflow.domain.item.event

import com.vrsalex.taskflow.domain.item.base.ItemRepository

interface EventRepository : ItemRepository<Event, EventCreate, EventUpdate>