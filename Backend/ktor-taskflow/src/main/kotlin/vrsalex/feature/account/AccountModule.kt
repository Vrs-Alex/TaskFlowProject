package vrsalex.feature.account

import org.koin.dsl.module
import vrsalex.core.security.UserIdProvider
import vrsalex.feature.account.data.repository.CachedUserIdProvider
import vrsalex.feature.account.data.repository.R2dbcRefreshTokenRepository
import vrsalex.feature.account.data.repository.R2dbcUserRepository
import vrsalex.feature.account.domain.service.AccountService
import vrsalex.feature.account.domain.service.JwtProvider
import vrsalex.feature.account.domain.repository.RefreshTokenRepository
import vrsalex.feature.account.domain.repository.UserRepository

val accountModule = module {

    single { JwtProvider(get()) }

    single<RefreshTokenRepository> { R2dbcRefreshTokenRepository() }

    single<UserRepository> { R2dbcUserRepository() }

    single<UserIdProvider> { CachedUserIdProvider(get()) }

    single { AccountService(get(), get(), get(), get(), get()) }

}