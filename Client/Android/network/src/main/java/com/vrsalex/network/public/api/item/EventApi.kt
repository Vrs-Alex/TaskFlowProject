package com.vrsalex.network.public.api.item

import com.vrsalex.network.public.common.SyncApi
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest

interface EventApi: SyncApi<EventDto, EventCreateRequest, EventUpdateRequest>