package com.vrsalex.network.public.provider

import kotlinx.coroutines.flow.Flow

interface DeviceIdProvider {

    suspend fun getDeviceId(): String

}