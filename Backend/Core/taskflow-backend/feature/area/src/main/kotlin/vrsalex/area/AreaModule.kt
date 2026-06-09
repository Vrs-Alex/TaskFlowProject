package vrsalex.area

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.area.data.AreaR2dbcRepository
import vrsalex.area.domain.AreaRepository
import vrsalex.area.domain.AreaService
import vrsalex.area.web.AreaRoute
import vrsalex.core.routing.AppRoute

val areaModule = module {

    single<AreaRepository> { AreaR2dbcRepository() }

    single { AreaService(get(), get(), get()) }

    single { AreaRoute() } bind AppRoute::class
}