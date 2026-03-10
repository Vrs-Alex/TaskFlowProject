package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import vrsalex.feature.auth.web.authRoute
import feature.account.web.userRoute

fun Application.configureRoute() {
    routing {
        route("/api") {
            authRoute()
            userRoute()
        }
    }
}