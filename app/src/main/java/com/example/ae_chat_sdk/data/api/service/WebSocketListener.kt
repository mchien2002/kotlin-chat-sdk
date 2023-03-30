package com.example.ae_chat_sdk.data.api.service

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener: WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
         webSocket.send("Hello")
        Log.e("burak", "connected")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        outPut("Closing: $code/ $reason")
    }

    private fun outPut(s: String) {
        Log.d("WebSocket", s)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        outPut("Received: $text")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        outPut("Error: "+t.message )
    }

    companion object{
        private const val NORMAL_CLOSURE_STATUS= 1000
    }
}