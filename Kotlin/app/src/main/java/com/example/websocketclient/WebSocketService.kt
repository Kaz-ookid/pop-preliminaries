package com.example.websocketclient

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>

    @Send
    fun sendNumber(number: Int)
}
