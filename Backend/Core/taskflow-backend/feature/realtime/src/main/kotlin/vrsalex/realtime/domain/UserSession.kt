package vrsalex.realtime.domain

import io.ktor.server.websocket.*

data class UserSession(
    val session: DefaultWebSocketServerSession,
    val deviceId: String?
)