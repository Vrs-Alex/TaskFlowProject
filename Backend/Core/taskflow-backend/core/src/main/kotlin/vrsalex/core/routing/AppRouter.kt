package vrsalex.core.routing

import io.ktor.server.routing.Route

interface AppRouter {

    fun Route.registerRoutes()

}