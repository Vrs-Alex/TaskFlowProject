package com.vrsalex.taskflow.data.remote

import com.vrsalex.network.public.provider.AuthObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthObserverImpl: AuthObserver {

    private val _logoutObserver = MutableSharedFlow<Unit>()
    override val logoutObserver: SharedFlow<Unit> = _logoutObserver.asSharedFlow()

    private val _isAuthorized = MutableSharedFlow<Boolean>()
    override val isAuthorized: SharedFlow<Boolean> =
        _isAuthorized.asSharedFlow()

    override suspend fun logout() {
        _logoutObserver.emit(Unit)
        _isAuthorized.emit(false)
    }

    override suspend fun setAuthorized(authorized: Boolean) {
        _isAuthorized.emit(authorized)
    }
}