package com.vrsalex.network.internal.impl

import com.vrsalex.network.public.api.RealtimeApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import vrsalex.shared.api.realtime.RealtimeEventDto
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.cancellation.CancellationException

internal class RealtimeApiImpl(
    private val client: HttpClient
) : RealtimeApi {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _messages = MutableSharedFlow<NetworkResult<RealtimeEventDto>>(extraBufferCapacity = 16)
    override val messages: SharedFlow<NetworkResult<RealtimeEventDto>> = _messages.asSharedFlow()

    private val _sendMessages = MutableSharedFlow<NetworkResult<RealtimeEventDto>>(extraBufferCapacity = 16)

    private val session = AtomicReference<DefaultWebSocketSession?>(null)

    override fun connect() {
        scope.launch {
            var exponentialDelay = 1000L
            while (isActive) {
                try {
                    client.webSocket("ws/realtime") {
                        session.set(this)
                        exponentialDelay = 1000L
                        launch {
                            _sendMessages.collect {
                                sendSerialized(it)
                            }
                        }
                        while (isActive) {
                            val dto = receiveDeserialized<RealtimeEventDto>()
                            _messages.emit(NetworkResult.Success(dto))
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                }
                catch (e: Exception) {
                    // _messages.emit(NetworkResult.Error(e))
                } finally {
                    session.set(null)
                    delay(exponentialDelay)
                    exponentialDelay = (exponentialDelay * 2).coerceAtMost(30_000L)
                }
            }
        }
    }

    override suspend fun disconnect() {
        session.get()?.close(CloseReason(CloseReason.Codes.NORMAL, "Disconnected"))
        session.set(null)
        scope.cancel()
    }

    override suspend fun send(message: RealtimeEventDto) {
        _sendMessages.emit(NetworkResult.Success(message))
    }
}