package vrsalex.core.routing

import io.ktor.server.routing.*

interface AppRoute {

    fun Route.registerRoutes()

}