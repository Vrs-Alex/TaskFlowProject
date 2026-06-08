package com.vrsalex.taskflow.domain.notify

interface PushTokenProvider {
    suspend fun getToken(): String
}