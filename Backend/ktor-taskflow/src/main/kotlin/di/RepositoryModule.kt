package vrsalex.di

import org.koin.dsl.module
import vrsalex.domain.repository.UserRepository
import vrsalex.infrastructure.persistence.repository.R2dbcUserRepository

val repositoryModule = module {

    single<UserRepository> { R2dbcUserRepository(get()) }

}