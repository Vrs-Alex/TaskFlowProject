package vrsalex.core.event_bus.data

import io.lettuce.core.RedisClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventChannels
import vrsalex.core.event_bus.domain.EventPublisher

class RedisEventPublisher(client: RedisClient) : EventPublisher {

    private val connection = client.connect()
    private val json = Json { classDiscriminator = "type" }


    override suspend fun publish(event: AppEvent) {
        withContext(Dispatchers.IO) {
            connection.sync().publish(event.channel(), json.encodeToString(event))
        }
    }

    private fun AppEvent.channel() = when (this) {
        is AppEvent.EntityChanged,
        is AppEvent.Logout           -> EventChannels.REALTIME
        is AppEvent.PushNotification -> EventChannels.NOTIFY
    }
}