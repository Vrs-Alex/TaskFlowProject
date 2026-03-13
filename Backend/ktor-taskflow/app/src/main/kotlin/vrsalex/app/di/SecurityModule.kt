package vrsalex.app.di

import org.koin.dsl.module
import vrsalex.core.security.BCryptPasswordHasher
import vrsalex.core.security.PasswordHasher

val securityModule = module {

    single<PasswordHasher> { BCryptPasswordHasher() }

}