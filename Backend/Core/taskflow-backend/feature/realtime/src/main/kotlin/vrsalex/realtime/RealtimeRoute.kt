package vrsalex.realtime

import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal

class RealtimeRoute : AppRouter {

    override fun Route.registerRoutes() {
        val sessionManager by inject<WebSocketSessionManager>()

        protected {
            webSocket("/ws/realtime") {
                val principal = call.principal<UserPrincipal>()
                val userId = principal?.internalId ?: return@webSocket close(
                    CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Unauthorized")
                )
                println("User $userId connected to WebSocket")
                sessionManager.registerSession(userId, this)

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