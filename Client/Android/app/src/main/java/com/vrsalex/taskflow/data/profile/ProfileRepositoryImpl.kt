package com.vrsalex.taskflow.data.profile

import com.vrsalex.taskflow.domain.profile.Profile
import com.vrsalex.taskflow.domain.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl: ProfileRepository {

    override fun getProfile(): Flow<Profile> {
        return flow {
            emit(
                Profile(
                    name = "Alex",
                    lastName = "Vrs",
                    email = "alexgm0508@gmail.com"
                )
            )
        }
    }

}