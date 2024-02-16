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
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow

class MainActivity : AppCompatActivity() {



    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webSocketClient = WebSocketClient(application)

        observeMessages()
        observeWebSocketEvents()

        val numberInput = findViewById<EditText>(R.id.numberInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val numberString = numberInput.text.toString()
            if (numberString.isNotEmpty()) {
                val number = numberString.toInt()
                webSocketClient.webSocketService.sendNumber(number)
                Toast.makeText(this, "Sent: $number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            webSocketClient.webSocketService.observeMessages()
                .asFlow()
                .collect { message ->
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Received: $message", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun observeWebSocketEvents() {
        lifecycleScope.launch {
            webSocketClient.webSocketService.observeWebSocketEvent()
                .asFlow()
                .collect { event ->
                    runOnUiThread {
                        when (event) {
                            is WebSocket.Event.OnConnectionOpened<*> -> {
                                Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
                            }
                            is WebSocket.Event.OnConnectionFailed -> {
                                Toast.makeText(this@MainActivity, "Connection Failed", Toast.LENGTH_SHORT).show()
                            }
                            is WebSocket.Event.OnConnectionClosed -> {
                                Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this@MainActivity, "Unknown Event", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }

}


