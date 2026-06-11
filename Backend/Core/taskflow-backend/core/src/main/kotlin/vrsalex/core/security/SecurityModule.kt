package vrsalex.core.security

import org.koin.dsl.module
import vrsalex.core.security.hash.PasswordHasher
import vrsalex.core.security.hash.TokenHasher
import vrsalex.core.security.hash.impl.BCryptPasswordHasher
import vrsalex.core.security.hash.impl.Sha256TokenHasher

val securityModule = module {

    single { BCryptPasswordHasher() as PasswordHasher }

    single<TokenHasher> { Sha256TokenHasher() }

}
