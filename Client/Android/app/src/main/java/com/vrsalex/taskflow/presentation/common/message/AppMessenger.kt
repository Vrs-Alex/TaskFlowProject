package com.vrsalex.taskflow.presentation.common.message

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class MessageType { ERROR, INFO, SUCCESS }

data class AppMessage(
    val text: UiText,
    val type: MessageType = MessageType.ERROR,
)

interface AppMessenger {
    val messages: SharedFlow<AppMessage>
    fun show(message: AppMessage)

    fun error(text: UiText) = show(AppMessage(text, MessageType.ERROR))
    fun info(text: UiText) = show(AppMessage(text, MessageType.INFO))
    fun success(text: UiText) = show(AppMessage(text, MessageType.SUCCESS))
}

class AppMessengerImpl : AppMessenger {
    private val _messages = MutableSharedFlow<AppMessage>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val messages: SharedFlow<AppMessage> = _messages.asSharedFlow()

    override fun show(message: AppMessage) {
        _messages.tryEmit(message)
    }
}
