package com.example.websocketclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webSocketClient = WebSocketClient(application)

        val numberInput = findViewById<EditText>(R.id.numberInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val numberString = numberInput.text.toString()
            if (numberString.isNotEmpty()) {
                val number = numberString.toInt()
                webSocketClient.webSocketService.sendNumber(number)
            }
        }

        observeMessages()
        observeWebSocketEvents()
    }

    private fun observeMessages() {
        webSocketClient.webSocketService.observeMessages()
            .onEach { message ->
                // Handle received message
                runOnUiThread {
                    Toast.makeText(this, "Received: $message", Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun observeWebSocketEvents() {
        webSocketClient.webSocketService.observeWebSocketEvent()
            .onEach { event ->
                when (event) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        runOnUiThread {
                            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is WebSocket.Event.OnConnectionFailed -> {
                        runOnUiThread {
                            Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is WebSocket.Event.OnConnectionClosed -> {
                        runOnUiThread {
                            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {}
                }
            }
            .launchIn(lifecycleScope)
    }

}


