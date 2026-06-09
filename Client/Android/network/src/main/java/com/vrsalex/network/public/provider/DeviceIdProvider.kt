package com.vrsalex.network.public.provider

interface DeviceIdProvider {

    suspend fun getDeviceId(): String

}