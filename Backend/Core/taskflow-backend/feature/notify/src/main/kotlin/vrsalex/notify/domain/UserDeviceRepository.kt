package vrsalex.notify.domain

import vrsalex.notify.domain.model.UserDevice

interface UserDeviceRepository {

    suspend fun add(device: UserDevice)

    suspend fun update(device: UserDevice)

    suspend fun deleteByToken(token: String)

}