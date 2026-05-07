package vrsalex.auth.data

import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.slf4j.LoggerFactory
import vrsalex.auth.domain.model.UserDevice
import vrsalex.auth.domain.repository.UserDeviceRepository
import vrsalex.core.database.UserDevicesTable
import vrsalex.core.database.utils.safeQuery
import kotlin.time.Clock

class UserDeviceRepositoryImpl: UserDeviceRepository {

    private val logger = LoggerFactory.getLogger(UserDeviceRepositoryImpl::class.java)

    override suspend fun add(device: UserDevice) = safeQuery(
        "Не удалось добавить устройство", logger
    ){
        val exists = UserDevicesTable.selectAll().where { UserDevicesTable.fcmToken eq device.fcmToken }.firstOrNull()
        if (exists != null) return@safeQuery
        UserDevicesTable.insert {
            it[UserDevicesTable.userId] = device.userId
            it[UserDevicesTable.fcmToken] = device.fcmToken
            it[UserDevicesTable.deviceName] = device.deviceName
            it[UserDevicesTable.createdAt] = Clock.System.now()
            it[UserDevicesTable.updatedAt] = Clock.System.now()
        }
    }

    override suspend fun deleteFromFcm(token: String) = safeQuery(
        "Не удалось удалить устройство по FCM токену", logger
    ){
        UserDevicesTable.deleteWhere { UserDevicesTable.fcmToken eq token }
        Unit
    }

}