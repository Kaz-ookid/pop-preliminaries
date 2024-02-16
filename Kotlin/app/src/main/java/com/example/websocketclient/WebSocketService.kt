package com.example.websocketclient

import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    @Receive
    fun observeWebSocketEvent(): Stream<WebSocket.Event>

    @Receive
    fun observeMessages(): Stream<String>

    @Send
    fun sendNumber(number: Int)
}
