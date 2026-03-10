package feature.account.di

import org.koin.dsl.module
import feature.account.data.repository.R2dbcRefreshTokenRepository
import feature.account.data.repository.R2dbcUserRepository
import feature.account.domain.service.AccountService
import feature.account.domain.service.JwtProvider
import feature.account.domain.repository.RefreshTokenRepository
import feature.account.domain.repository.UserRepository

val accountModule = module {

    single { JwtProvider(get()) }

    single<RefreshTokenRepository> { R2dbcRefreshTokenRepository(get()) }

    single<UserRepository> { R2dbcUserRepository(get()) }


    single { AccountService(get(), get(), get(), get(), get()) }

}