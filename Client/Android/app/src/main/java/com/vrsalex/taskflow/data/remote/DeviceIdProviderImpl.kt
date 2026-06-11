package com.vrsalex.taskflow.data.remote

import android.content.Context
import android.provider.Settings
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.Uuid

class DeviceIdProviderImpl(
    private val context: Context,
    private val dataStoreManager: DataStoreManager
) : DeviceIdProvider {

    @Volatile
    private var cachedDeviceId: String? = null
    private val mutex = Mutex()

    override suspend fun getDeviceId(): String {
        cachedDeviceId?.let { return it }

        return mutex.withLock {
            cachedDeviceId ?: run {
                val id = resolveDeviceId()
                cachedDeviceId = id
                id
            }
        }
    }

    private suspend fun resolveDeviceId(): String {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (!androidId.isNullOrBlank() && androidId != FAKE_ANDROID_ID) {
            return androidId
        }

        val stored = dataStoreManager.getDeviceId().first()
        return stored ?: Uuid.random().toString()
            .also { dataStoreManager.saveDeviceId(it) }
    }

    private companion object {
        const val FAKE_ANDROID_ID = "9774d56d682e549c"
    }
}