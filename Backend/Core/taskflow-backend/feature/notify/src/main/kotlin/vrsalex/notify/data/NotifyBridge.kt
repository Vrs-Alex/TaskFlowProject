package vrsalex.notify.data

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventChannels
import vrsalex.notify.domain.NotifyRepository


class NotifyBridge(
    redisClient: RedisClient,
    private val notifyRepository: NotifyRepository,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val json = Json { classDiscriminator = "type" }
    private val logger = LoggerFactory.getLogger(NotifyBridge::class.java)

    init {
        val connection = redisClient.connectPubSub()
        connection.addListener(object : RedisPubSubAdapter<String, String>() {
            override fun message(channel: String, message: String) {
                scope.launch {
                    try {
                        val event = json.decodeFromString<AppEvent>(message)
                        handleEvent(event)
                    } catch (e: Exception) {
                        logger.error("Failed to process event: $message", e)
                    }
                }
            }
        })
        connection.async().subscribe(EventChannels.NOTIFY)
    }

    private suspend fun handleEvent(event: AppEvent) {
        when (event) {
            is AppEvent.PushNotification -> {
                println("TEST")
                notifyRepository.sendPush(
                    userId = event.userId,
                    title = event.title,
                    description = event.description,
                    excludeDeviceId = event.sourceDeviceId
                )
            }
            else -> {  }
        }
    }
}