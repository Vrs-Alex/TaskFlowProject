package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vrsalex.feature.account.web.authRoute
import vrsalex.feature.account.web.userRoute
import vrsalex.feature.workspace.web.areaRoute
import vrsalex.feature.workspace.web.tagRoute

fun Application.configureRoute() {
    routing {
        route("/api") {

            // Account
            authRoute()
            userRoute()


            // Workspace
            areaRoute()
            tagRoute()


        }
    }
}


