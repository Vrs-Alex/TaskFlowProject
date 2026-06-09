package com.vrsalex.taskflow.data.notify

import android.os.Build
import com.vrsalex.network.public.api.NotifyApi
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.taskflow.domain.auth.DeviceInfo
import com.vrsalex.taskflow.domain.auth.PushPlatform
import com.vrsalex.taskflow.domain.auth.toDto
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import com.vrsalex.taskflow.domain.notify.NotifyRepository
import com.vrsalex.taskflow.domain.notify.PushTokenProvider
import kotlinx.coroutines.flow.first
import vrsalex.shared.api.notify.RegisterDeviceRequest

class NotifyRepositoryImpl(
    private val notifyApi: NotifyApi,
    private val deviceIdProvider: DeviceIdProvider,
    private val pushTokenProvider: PushTokenProvider,
    private val dataStoreManager: DataStoreManager
): NotifyRepository {

     private suspend fun upsertDevice(data: DeviceInfo): Resource<Unit> {
        return notifyApi.upsertDeviceInfo(
            RegisterDeviceRequest(
                deviceId = data.deviceId,
                platform = data.platform.toDto(),
                token = data.token,
                deviceName = data.deviceName ?: "Android",
                isPushEnabled = data.isNotify
                )
        ).toResource { Unit }
    }


    override suspend fun registerDevice(token: String?, isNotify: Boolean?): Resource<Unit> {
        val deviceId = deviceIdProvider.getDeviceId()
        val token = token ?: pushTokenProvider.getToken()
        val isNotify = isNotify ?: dataStoreManager.isPushEnabled().first()
        val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}".replaceFirstChar { it.uppercase() }

        if (token.isEmpty()) return Resource.Failure.Error("FCM token not found")
        return upsertDevice(
            DeviceInfo(
                platform = PushPlatform.FCM,
                token = token,
                deviceId = deviceId,
                deviceName = deviceName,
                isNotify = isNotify
            )
        )
    }

}