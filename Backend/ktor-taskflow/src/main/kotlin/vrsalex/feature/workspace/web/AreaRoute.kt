package vrsalex.feature.workspace.web

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.areaRoute() {

    authenticate("auth-jwt") {

        route("/area") {



        }
    }
}