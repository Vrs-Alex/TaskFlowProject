package vrsalex.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import vrsalex.web.routes.userRoute

fun Application.configureRoute() {
    routing {
        userRoute()

    }
}