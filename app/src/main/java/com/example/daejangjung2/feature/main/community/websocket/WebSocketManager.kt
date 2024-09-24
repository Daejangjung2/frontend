package com.example.daejangjung2.feature.main.community.websocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketManager {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun startWebSocket() {
        val request = Request.Builder()
            .url("ws://yourserver.com/websocket")  // 서버의 웹소켓 URL
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                // 서버와 연결되었을 때
                println("WebSocket Opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // 서버로부터 메시지를 받았을 때
                println("Message received: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 서버로부터 바이너리 메시지를 받았을 때
                println("Binary message received: $bytes")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // 웹소켓이 닫혔을 때
                println("WebSocket Closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                // 에러 발생 시
                t.printStackTrace()
            }
        })

        // 비동기 방식이므로, 콜백을 사용해 상태 변화에 대응
    }

    fun sendMessage(message: String) {
        webSocket.send(message)  // 서버로 메시지 전송
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Closing connection")  // 웹소켓 종료
    }
}
