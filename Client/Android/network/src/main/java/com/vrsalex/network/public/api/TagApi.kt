package com.vrsalex.network.public.api

import com.vrsalex.network.public.common.SyncApi
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest


interface TagApi: SyncApi<TagDto, TagCreateRequest, TagUpdateRequest>