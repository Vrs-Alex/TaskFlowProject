package vrsalex.realtime.web

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.realtime.data.WebSocketRealtimePublisher

class RealtimeRoute : AppRoute {

    override fun Route.registerRoutes() {
        val sessionManager by inject<WebSocketRealtimePublisher>()

        protected {
            webSocket("/ws/realtime") {
                val principal = call.principal<UserPrincipal>()
                val userId = principal?.internalId ?: return@webSocket close(
                    CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Unauthorized")
                )
                val deviceId = call.request.headers["X-Device-Id"]
                sessionManager.registerSession(userId, this, deviceId)
                println("User $userId connected to WebSocket")

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {

                        }
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                } finally {
                    sessionManager.unregisterSession(userId, this)
                    println("User $userId disconnected from WebSocket")
                }
            }
        }
    }
}