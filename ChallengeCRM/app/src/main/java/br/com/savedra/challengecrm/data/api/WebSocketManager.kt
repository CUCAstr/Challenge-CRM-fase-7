package br.com.savedra.challengecrm.data.api

import android.util.Log
import okhttp3.*
import org.json.JSONObject

class WebSocketManager(private val baseUrl: String) {
    private var client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(onMessageReceived: (String) -> Unit) {
        val request = Request.Builder().url(baseUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessageReceived(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Goodbye")
    }
}
