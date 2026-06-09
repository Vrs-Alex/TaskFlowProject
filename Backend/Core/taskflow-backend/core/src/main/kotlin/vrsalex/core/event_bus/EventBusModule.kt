package vrsalex.core.event_bus

import io.lettuce.core.RedisClient
import org.koin.dsl.module
import vrsalex.core.event_bus.data.RedisEventPublisher
import vrsalex.core.event_bus.domain.EventPublisher

val eventBusModule = module {

    single<RedisClient> {
        val host = System.getenv("REDIS_HOST") ?: "localhost"
        val port = System.getenv("REDIS_PORT") ?: "6379"
        val password = System.getenv("REDIS_PASSWORD")?.takeIf { it.isNotBlank() }
        val uri = if (password != null)
            "redis://$password@$host:$port"
        else
            "redis://$host:$port"
        RedisClient.create(uri)
    }

    single<EventPublisher> { RedisEventPublisher(get()) }

}