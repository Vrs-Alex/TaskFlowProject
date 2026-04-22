package com.vrsalex.taskflow.domain.common

import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.HomeDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.OnBoardingDestination
import com.vrsalex.taskflow.presentation.navigation.SignInDestination
import kotlinx.coroutines.flow.first

class GetStartDestinationUseCase(
    private val dataStoreManager: DataStoreManager
) {

    suspend operator fun invoke(): Any {
        val isFirstLaunch = dataStoreManager.isFirstLaunch().first()
        return when (isFirstLaunch){
            true -> {
//                dataStoreManager.setFirstLaunch()
                OnBoardingDestination
            }
            false -> {
                val accessToken = dataStoreManager.getAccessToken().first()
                if (accessToken != null)
                    MainGraph
                else
                    AuthGraph
            }
        }
    }

}