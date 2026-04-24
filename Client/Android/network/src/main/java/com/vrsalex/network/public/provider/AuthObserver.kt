package com.vrsalex.network.public.provider

import kotlinx.coroutines.flow.SharedFlow

interface AuthObserver {

    val logoutObserver: SharedFlow<Unit>

    val isAuthorized: SharedFlow<Boolean>

    suspend fun logout()

    suspend fun setAuthorized(authorized: Boolean)

}