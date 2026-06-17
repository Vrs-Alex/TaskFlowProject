package com.vrsalex.taskflow.domain.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile(): Flow<Profile>

}