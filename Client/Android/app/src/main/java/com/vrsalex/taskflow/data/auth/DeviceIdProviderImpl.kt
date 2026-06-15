package com.vrsalex.taskflow.data.auth

import android.content.Context
import android.provider.Settings
import com.vrsalex.network.public.provider.DeviceIdProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.Uuid

class DeviceIdProviderImpl(
    private val context: Context,
) : DeviceIdProvider {

    @Volatile
    private var cached: String? = null
    private val mutex = Mutex()

    override suspend fun getDeviceId(): String {
        cached?.let { return it }
        return mutex.withLock {
            cached ?: resolve().also { cached = it }
        }
    }

    private fun resolve(): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return if (!androidId.isNullOrBlank() && androidId != FAKE_ANDROID_ID) {
            androidId
        } else {
            Uuid.random().toString()
        }
    }

    private companion object {
        const val FAKE_ANDROID_ID = "9774d56d682e549c"
    }
}
