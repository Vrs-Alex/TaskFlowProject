package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import vrsalex.feature.auth.web.authRoute
import vrsalex.feature.account.web.userRoute
import vrsalex.feature.workspace.web.areaRoute

fun Application.configureRoute() {
    routing {
        route("/api") {

            // Account
            authRoute()
            userRoute()


            // Workspace
            areaRoute()


        }
    }
}