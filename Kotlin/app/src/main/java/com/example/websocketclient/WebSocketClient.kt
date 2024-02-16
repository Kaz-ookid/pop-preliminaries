package com.example.websocketclient

import android.app.Application
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient

class WebSocketClient(application: Application) {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val scarletInstance = Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory("ws://10.0.2.2:8080/echo"))
        .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
        .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
        .build()

    val webSocketService: WebSocketService = scarletInstance.create()
}