package vrsalex.core.security

import org.koin.dsl.module
import vrsalex.core.security.hash.BCryptPasswordHasher
import vrsalex.core.security.hash.PasswordHasher

val securityModule = module {

    single { BCryptPasswordHasher() as PasswordHasher }

}
