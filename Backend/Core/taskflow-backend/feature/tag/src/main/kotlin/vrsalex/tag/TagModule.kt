package vrsalex.tag

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRouter
import vrsalex.tag.data.TagR2dbcRepository
import vrsalex.tag.domain.TagRepository
import vrsalex.tag.domain.TagService
import vrsalex.tag.web.TagRoute

val tagModule = module {

    single<TagRepository>{ TagR2dbcRepository() }

    single { TagService(get(), get(), get()) }

    single { TagRoute() } bind AppRouter::class

}