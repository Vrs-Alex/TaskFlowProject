package vrsalex.core.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {

    private val _events = MutableSharedFlow<AppEvent>()
    val events: SharedFlow<AppEvent> = _events.asSharedFlow()

    suspend fun publish(event: AppEvent) {
        _events.emit(event)
    }

     suspend inline fun <reified T : AppEvent> subscribe(crossinline onEvent: (T) -> Unit) {
        events.collect { event ->
            if (event is T) {
                onEvent(event)
            }
        }
    }

}