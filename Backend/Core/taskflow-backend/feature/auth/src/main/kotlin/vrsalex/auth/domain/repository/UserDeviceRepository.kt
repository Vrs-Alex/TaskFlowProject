package vrsalex.auth.domain.repository

import vrsalex.auth.domain.model.UserDevice

interface UserDeviceRepository {

    suspend fun add(device: UserDevice)

    suspend fun deleteByFcm(token: String)

}