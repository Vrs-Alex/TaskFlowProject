package vrsalex.realtime.data

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.realtime.domain.UserSession
import vrsalex.shared.api.realtime.RealtimeEventDto
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class WebSocketRealtimePublisher : RealtimeEventPublisher {
    private val userSessions = ConcurrentHashMap<Long, MutableSet<UserSession>>()

    fun registerSession(userId: Long, session: DefaultWebSocketServerSession, deviceId: String?) {
        userSessions.computeIfAbsent(userId) {
            Collections.synchronizedSet(LinkedHashSet())
        }.add(UserSession(session, deviceId))
    }

    fun unregisterSession(userId: Long, session: DefaultWebSocketServerSession) {
        userSessions.computeIfPresent(userId) { _, set ->
            set.removeIf { it.session == session }
            if (set.isEmpty()) null else set
        }
    }

    override suspend fun sendEvent(userId: Long, event: RealtimeEventDto, excludeDeviceId: String) {
        userSessions[userId]?.forEach { userSession ->
            // Скип уст-ва которое прислало изменения (является источником)
            if (userSession.deviceId == excludeDeviceId) return@forEach
            try {
                userSession.session.sendSerialized(event)
            } catch (e: Exception) {
                print(e)
            }
        }
    }

    override suspend fun broadcast(event: RealtimeEventDto) {
        userSessions.values.flatten().forEach { userSession ->
            try {
                userSession.session.sendSerialized(event)
            } catch (e: Exception) { }
        }
    }
}