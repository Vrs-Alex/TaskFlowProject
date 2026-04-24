package com.vrsalex.taskflow.domain.common.notify

import kotlinx.coroutines.flow.SharedFlow

interface AppMessenger {

    val messages: SharedFlow<AppMessage>

    suspend fun sendMessage(message: AppMessage)

}

sealed class AppMessage {

    data class Notification(
        val message: String,
        val type: MessageType
    ): AppMessage()

}

enum class MessageType {
    INFO, ERROR, Success
}