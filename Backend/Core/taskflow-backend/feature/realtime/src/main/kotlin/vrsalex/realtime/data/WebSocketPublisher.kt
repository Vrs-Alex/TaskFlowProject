package vrsalex.realtime.data

import io.ktor.server.websocket.*
import vrsalex.realtime.domain.RealtimeEventPublisher
import vrsalex.realtime.domain.UserSession
import vrsalex.shared.api.realtime.RealtimeEventDto
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class WebSocketPublisher : RealtimeEventPublisher {
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

    override suspend fun sendEvent(userId: Long, event: RealtimeEventDto, sourceDeviceId: String) {
        userSessions[userId]?.forEach { userSession ->
            if (userSession.deviceId == sourceDeviceId) return@forEach
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