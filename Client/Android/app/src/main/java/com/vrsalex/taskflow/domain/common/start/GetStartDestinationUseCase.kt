package com.vrsalex.taskflow.domain.common.start

import com.vrsalex.taskflow.data.local.datastore.DataStoreManager
import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.OnBoardingDestination
import kotlinx.coroutines.flow.first

class GetStartDestinationUseCase(
    private val dataStoreManager: DataStoreManager
) {

    suspend operator fun invoke(): Any {
        val refreshToken = dataStoreManager.getRefreshToken().first()
        return when(refreshToken){
            null -> {
                val isFirstLaunch = dataStoreManager.getFirstLaunch().first()
                if (isFirstLaunch == true || isFirstLaunch == null) OnBoardingDestination
                else AuthGraph
            }
            else -> MainGraph
        }
    }

}