package com.vrsalex.taskflow.domain.notify

import com.vrsalex.taskflow.domain.common.model.Resource

interface NotifyRepository {

    suspend fun registerDevice(token: String? = null, isNotify: Boolean? = null): Resource<Unit>

}