package com.vrsalex.taskflow.presentation.common

import com.vrsalex.taskflow.domain.common.AppMessage
import com.vrsalex.taskflow.domain.common.AppMessenger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow



class AppMessengerImpl: AppMessenger {

    private val _messages = MutableSharedFlow<AppMessage>(extraBufferCapacity = 4)
    override val messages: SharedFlow<AppMessage> = _messages.asSharedFlow()

    override suspend fun sendMessage(message: AppMessage) {
        _messages.emit(message)
    }
}