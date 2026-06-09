package vrsalex.app.plugin

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin
import vrsalex.core.routing.AppRoute


fun Application.configureRoute() {

    val routers = getKoin().getAll<AppRoute>()
    routing {
        route("/api/v1") {
            routers.forEach { router ->
                with(router) { registerRoutes() }
            }
        }
    }
}


