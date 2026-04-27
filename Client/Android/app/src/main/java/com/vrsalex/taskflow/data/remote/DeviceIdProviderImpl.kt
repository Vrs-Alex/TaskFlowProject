package com.vrsalex.taskflow.data.remote

import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.uuid.Uuid

class DeviceIdProviderImpl(private val dataStoreManager: DataStoreManager): DeviceIdProvider {
    private var cachedDeviceId: String? = null

    override suspend fun getDeviceId(): String {
        return cachedDeviceId ?: run {
            val existing = dataStoreManager.getDeviceId().first()
            val deviceId = existing ?: Uuid.random().toString()
                .also { dataStoreManager.saveDeviceId(it) }
            cachedDeviceId = deviceId
            deviceId
        }
    }
}