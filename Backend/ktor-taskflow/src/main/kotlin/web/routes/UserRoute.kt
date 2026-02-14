package vrsalex.web.routes

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.userRoute(){

    route("/user") {

        get {
            call.respondText { "Hi there!" }
        }

    }

}