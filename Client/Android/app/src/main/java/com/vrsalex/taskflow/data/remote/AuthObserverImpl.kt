package com.vrsalex.taskflow.data.remote

import com.vrsalex.network.public.provider.AuthObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthObserverImpl: AuthObserver {

    private val _observer = MutableSharedFlow<Unit>()
    override val observer: SharedFlow<Unit> = _observer.asSharedFlow()

    override suspend fun logout() {
        _observer.emit(Unit)
    }
}