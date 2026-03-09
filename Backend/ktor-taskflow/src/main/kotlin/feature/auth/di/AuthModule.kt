package vrsalex.feature.auth.di

import org.koin.dsl.module
import vrsalex.feature.auth.data.repository.R2dbcRefreshTokenRepository
import vrsalex.feature.auth.data.repository.R2dbcUserRepository
import vrsalex.feature.auth.domain.service.AuthService
import vrsalex.feature.auth.domain.service.JwtProvider
import vrsalex.feature.auth.domain.repository.RefreshTokenRepository
import vrsalex.feature.auth.domain.repository.UserRepository

val authModule = module {

    single { JwtProvider(get()) }

    single<RefreshTokenRepository> { R2dbcRefreshTokenRepository(get()) }

    single<UserRepository> { R2dbcUserRepository(get()) }


    single { AuthService(get(), get(), get(), get(), get()) }

}