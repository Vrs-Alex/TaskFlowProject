package feature.account.web


import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.userRoute(){

    authenticate("auth-jwt") {

        route("/user") {

        }

    }

}