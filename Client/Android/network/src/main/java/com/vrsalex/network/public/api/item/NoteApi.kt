package com.vrsalex.network.public.api.item

import com.vrsalex.network.public.common.SyncApi
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest

interface NoteApi : SyncApi<ItemDto, ItemCreateRequest, ItemUpdateRequest>
