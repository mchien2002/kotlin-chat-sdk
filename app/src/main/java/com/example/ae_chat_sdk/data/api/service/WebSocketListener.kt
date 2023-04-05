package com.example.ae_chat_sdk.data.api.service

import android.util.Log
import com.example.ae_chat_sdk.data.model.SocketRequest
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

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


    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        outPut("Received bytes $bytes")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        outPut("Error: " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }


    fun receiveMessage(webSocket: WebSocket) {

        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map["groupId"] = "4028818b86a7ca870186a7cc2bfd0003"
        val request: SocketRequest = SocketRequest("list_message", "1", map)

        val gson = Gson()
        webSocket.send(gson.toJson(request))
    }
}