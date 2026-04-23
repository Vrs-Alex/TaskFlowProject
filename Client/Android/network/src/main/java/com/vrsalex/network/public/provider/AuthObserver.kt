package com.vrsalex.network.public.provider

import kotlinx.coroutines.flow.SharedFlow

interface AuthObserver {

    val observer: SharedFlow<Unit>

    suspend fun logout()

}