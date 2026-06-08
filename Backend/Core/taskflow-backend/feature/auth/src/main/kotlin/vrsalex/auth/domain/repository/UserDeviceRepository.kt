package vrsalex.auth.domain.repository

import vrsalex.auth.domain.model.device.UserDevice

interface UserDeviceRepository {

    suspend fun add(device: UserDevice)

    suspend fun update(device: UserDevice)

    suspend fun deleteByToken(token: String)

}