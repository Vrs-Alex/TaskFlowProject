package vrsalex.realtime

import io.ktor.server.websocket.DefaultWebSocketServerSession

data class UserSession(
    val session: DefaultWebSocketServerSession,
    val deviceId: String?
)