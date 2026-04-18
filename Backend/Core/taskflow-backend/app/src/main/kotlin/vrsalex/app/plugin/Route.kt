package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.getKoin
import vrsalex.core.routing.AppRouter


fun Application.configureRoute() {

    val routers = getKoin().getAll<AppRouter>()
    routing {
        route("/api/v1") {
            routers.forEach { router ->
                with(router) { registerRoutes() }
            }
        }
    }
}


