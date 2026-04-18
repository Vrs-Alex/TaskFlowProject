package vrsalex.auth

import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import vrsalex.auth.data.CachedUserIdProvider
import vrsalex.auth.data.R2dbcRefreshTokenRepository
import vrsalex.auth.data.UserR2dbcRepository
import vrsalex.auth.domain.repository.RefreshTokenRepository
import vrsalex.auth.domain.repository.UserRepository
import vrsalex.auth.domain.service.AuthService
import vrsalex.auth.domain.service.JwtProvider
import vrsalex.auth.web.AuthRouter
import vrsalex.core.routing.AppRouter
import vrsalex.core.security.user.UserIdProvider

val authModule = module {

    single { JwtProvider(get()) }

    single<UserIdProvider> { CachedUserIdProvider(get(), get()) }

    single<UserRepository> { UserR2dbcRepository() }

    single<RefreshTokenRepository> { R2dbcRefreshTokenRepository() }

    single { AuthService(get(), get(), get(), get(), get()) }

    single { AuthRouter(get()) } bind AppRouter::class
}


