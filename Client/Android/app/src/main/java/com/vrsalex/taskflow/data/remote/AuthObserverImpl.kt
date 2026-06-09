package com.vrsalex.taskflow.data.remote

import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

class AuthObserverImpl(
    private val appDatabase: AppDatabase,
    private val dataStoreManager: DataStoreManager
): AuthObserver {

    private val _logoutObserver = MutableSharedFlow<Unit>()
    override val logoutObserver: SharedFlow<Unit> = _logoutObserver.asSharedFlow()

    private val _isAuthorized = MutableSharedFlow<Boolean>()
    override val isAuthorized: SharedFlow<Boolean> =
        _isAuthorized.asSharedFlow()

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            appDatabase.clearAll()
            dataStoreManager.clearAll()
        }
        _logoutObserver.emit(Unit)
        _isAuthorized.emit(false)
    }

    override suspend fun setAuthorized(authorized: Boolean) {
        _isAuthorized.emit(authorized)
    }
}