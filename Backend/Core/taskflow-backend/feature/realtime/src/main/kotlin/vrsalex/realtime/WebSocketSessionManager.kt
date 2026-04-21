package vrsalex.realtime

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.shared.api.realtime.RealtimeEventDto
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class WebSocketSessionManager : RealtimeEventPublisher {

    private val userSessions = ConcurrentHashMap<Long, MutableSet<DefaultWebSocketServerSession>>()

    fun registerSession(userId: Long, session: DefaultWebSocketServerSession) {
        userSessions.computeIfAbsent(userId) {
            Collections.synchronizedSet(LinkedHashSet())
        }.add(session)
    }

    fun unregisterSession(userId: Long, session: DefaultWebSocketServerSession) {
        userSessions.computeIfPresent(userId) { _, set ->
            set.remove(session)
            if (set.isEmpty()) null else set
        }
    }

    override suspend fun sendEvent(userId: Long, event: RealtimeEventDto) {
        print("Отправляю событие юзеру $userId. Найдено сессий: ${userSessions.size}")
        userSessions[userId]?.forEach { session ->
            try {
                // TODO remove print
                println("Отправляю событие юзеру $userId. Найдено сессий: ${userSessions.size}")
                session.sendSerialized(event)
            } catch (e: Exception) {
                print(e)
            }
        }
    }

    override suspend fun broadcast(event: RealtimeEventDto) {
        userSessions.values.flatten().forEach { session ->
            try {
                session.sendSerialized(event)
            } catch (e: Exception) { }
        }
    }
}