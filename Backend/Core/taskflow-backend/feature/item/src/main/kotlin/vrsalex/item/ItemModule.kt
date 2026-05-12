package vrsalex.item

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRouter
import vrsalex.item.data.ItemR2dbcRepository
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.item.domain.service.ItemService
import vrsalex.item.web.ItemRoute

val itemModule = module {


    single<ItemRepository> { ItemR2dbcRepository() }

    single { ItemService(get(), get(), get()) }

    single<ItemRoute> { ItemRoute() } bind AppRouter::class

}