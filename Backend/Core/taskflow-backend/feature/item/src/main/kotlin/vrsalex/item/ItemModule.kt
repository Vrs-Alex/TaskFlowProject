package vrsalex.item

import org.koin.dsl.module
import vrsalex.item.data.ItemR2dbcRepository
import vrsalex.item.domain.repository.ItemRepository

val itemModule = module {


    single<ItemRepository> { ItemR2dbcRepository() }



}