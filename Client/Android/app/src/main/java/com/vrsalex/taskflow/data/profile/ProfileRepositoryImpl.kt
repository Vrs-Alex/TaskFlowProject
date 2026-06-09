package com.vrsalex.taskflow.data.profile

import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import com.vrsalex.taskflow.domain.notify.NotifyRepository
import com.vrsalex.taskflow.domain.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val notifyRepository: NotifyRepository
): ProfileRepository {


    override suspend fun setPushEnabled(boolean: Boolean) {
        dataStoreManager.setPushEnabled(boolean)
        notifyRepository.registerDevice(token = null, isNotify = boolean)
    }

    override fun isPushEnabled(): Flow<Boolean> = dataStoreManager.isPushEnabled()



}