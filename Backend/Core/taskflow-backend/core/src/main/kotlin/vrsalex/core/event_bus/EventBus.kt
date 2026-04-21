package vrsalex.core.event_bus

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

class EventBus {

    private val _events = MutableSharedFlow<EventBusData>(
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<EventBusData> = _events.asSharedFlow()

    suspend fun publish(event: EventBusData) {
        withContext(Dispatchers.IO) {
            _events.emit(event)
        }
    }

    suspend inline fun <reified T : EventBusData> subscribe(crossinline onEvent: (T) -> Unit) {
        events.collect { event ->
            if (event is T) {
                onEvent(event)
            }
        }
    }

}