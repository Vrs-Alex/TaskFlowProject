package com.vrsalex.taskflow.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.domain.common.start.GetStartDestinationUseCase
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.domain.sync.SyncUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntyPointViewModel(
    private val getStartDestinationUseCase: GetStartDestinationUseCase,
    private val syncUseCase: SyncUseCase,
    val authObserver: AuthObserver,
    private val appDatabase: AppDatabase,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val startDestination = getStartDestinationUseCase()
            _startDestination.update { startDestination }

            if (startDestination == MainGraph) {
                authObserver.setAuthorized(true)
            }
        }

        viewModelScope.launch {
            authObserver.logoutObserver.collect {
                withContext(Dispatchers.IO) {
                    appDatabase.clearAll()
                    dataStoreManager.clearAll()
                }
            }
        }
    }

}