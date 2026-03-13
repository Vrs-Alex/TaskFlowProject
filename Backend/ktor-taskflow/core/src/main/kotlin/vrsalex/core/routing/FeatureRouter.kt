package vrsalex.core.routing

import io.ktor.server.routing.Route

interface FeatureRouter {

    fun Route.registerRoutes()

}