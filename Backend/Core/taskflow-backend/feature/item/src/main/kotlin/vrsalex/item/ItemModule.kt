package vrsalex.item

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRoute
import vrsalex.item.data.ItemR2dbcRepository
import vrsalex.item.data.NoteR2dbcRepository
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.item.domain.repository.NoteRepository
import vrsalex.item.domain.service.NoteService
import vrsalex.item.web.NoteRoute

val itemModule = module {


    single<ItemRepository> { ItemR2dbcRepository() }

    single<NoteRepository> { NoteR2dbcRepository() }

    single { NoteService(get(), get(), get()) }

    single<NoteRoute> { NoteRoute() } bind AppRoute::class

}