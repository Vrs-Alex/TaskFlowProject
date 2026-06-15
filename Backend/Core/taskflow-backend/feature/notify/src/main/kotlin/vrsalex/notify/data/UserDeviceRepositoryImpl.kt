package vrsalex.notify.data

import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import org.slf4j.LoggerFactory
import vrsalex.core.database.UserDeviceTable
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.notify.domain.UserDeviceRepository
import vrsalex.notify.domain.model.UserDevice
import kotlin.time.Clock

class UserDeviceRepositoryImpl: UserDeviceRepository {

    private val logger = LoggerFactory.getLogger(UserDeviceRepositoryImpl::class.java)

    override suspend fun add(device: UserDevice) = safeQuery(
        "Не удалось добавить устройство", logger
    ) {
        val exists = UserDeviceTable.selectAll().where {
            (UserDeviceTable.deviceId eq device.deviceId)
        }.firstOrNull()

        if (exists != null) {
            update(device)
            return@safeQuery
        }

        UserDeviceTable.insert {
            it[UserDeviceTable.userId] = device.userId
            it[UserDeviceTable.platform] = device.platform.name
            it[UserDeviceTable.token] = device.token
            it[UserDeviceTable.deviceId] = device.deviceId
            it[UserDeviceTable.deviceName] = device.deviceName
            it[UserDeviceTable.pushEnabled] = device.isPushEnabled
            it[UserDeviceTable.createdAt] = Clock.System.now()
            it[UserDeviceTable.updatedAt] = Clock.System.now()
        }
    }

    override suspend fun update(device: UserDevice) = safeQuery(
        "Не удалось обновить токен",
        logger
    ) {
        val table = UserDeviceTable
        val updatedRows = table.update(
            {
                (table.deviceId eq device.deviceId) and (table.userId eq device.userId)
            }
        ) { statement ->
            statement[table.token] = device.token
            statement[table.pushEnabled] = device.isPushEnabled
            statement[table.updatedAt] = Clock.System.now()
        }

        if (updatedRows == 0) throw AppException.BadRequest("Не удалось обновить токен")
    }

    override suspend fun deleteByToken(token: String) = safeQuery(
        "Не удалось удалить устройство по токену", logger
    ) {
        UserDeviceTable.deleteWhere { UserDeviceTable.token eq token }
        Unit
    }

}