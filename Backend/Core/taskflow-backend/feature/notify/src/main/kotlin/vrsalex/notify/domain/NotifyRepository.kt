package vrsalex.notify.domain

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.slf4j.LoggerFactory
import vrsalex.core.database.UserDeviceTable
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.database.utils.safeQuery
import vrsalex.notify.data.FcmProvider
import kotlin.math.log

class NotifyRepository(
    private val fcmProvider: FcmProvider,
    private val transactionManager: TransactionManager
) {
    val logger = LoggerFactory.getLogger(this::class.java)!!

    suspend fun sendPush(
        title: String,
        description: String,
        userId: Long,
        excludeDeviceId: String
    ) {
        val pushInfo = getUserTokens(userId, excludeDeviceId)

        pushInfo.groupBy { it.platform }.forEach { (platform, items) ->
            val tokens = items.map { it.token }
            when (platform) {
                PushPlatform.FCM -> {
                    logger.info("${pushInfo.size} push notifications for $title")
                    fcmProvider.sendPush(tokens, title, description)
                    logger.info("after")
                }
                PushPlatform.APNS -> TODO()
                PushPlatform.WEB_PUSH -> TODO()
                PushPlatform.WNS -> TODO()
            }
        }
    }

    private suspend fun getUserTokens(
        userId: Long,
        excludeDeviceId: String
    ) = safeQuery(
        error = "Не удалось получить токены",
        logger = logger
    ){
        transactionManager.dbTransaction {
            UserDeviceTable.selectAll()
                .where {
                    (UserDeviceTable.deviceId neq excludeDeviceId) and (UserDeviceTable.userId eq userId)
                }
                .map { it.toPushInfo() }
                .toList()
        }
    }

    private fun ResultRow.toPushInfo(): PushInfo = PushInfo(
        platform = PushPlatform.valueOf(this[UserDeviceTable.platform]),
        token = this[UserDeviceTable.token]
    )


}