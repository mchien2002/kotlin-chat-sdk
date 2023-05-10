package com.example.ae_chat_sdk.data.api.service

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.acti.home.HomeActivity.Companion.recentAdapter
import com.example.ae_chat_sdk.data.model.Group
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.data.model.SocketRequest
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.socket.SocketRequestType
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.FileInputStream
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WebSocketListener : WebSocketListener() {


    override fun onOpen(webSocket: WebSocket, response: Response) {
        myWebSocket = webSocket
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance()
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        val userProfile: MutableMap<String, Any> = HashMap<String, Any>()
        userProfile["userProfile"] = user
        val socketRequest: SocketRequest =
            SocketRequest(event = "register_session_user", userProfile)
        val jsonString = gson.toJson(socketRequest)
        webSocket.send(jsonString)
        getListGroup(webSocket,user.userId)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        val response = gson.fromJson(text, SocketRequest::class.java)
        if (response.event == SocketRequestType.SOCKET_REQUEST_LIST_GROUP) {
            android.os.Handler(Looper.getMainLooper()).post {
                val typeToken = object : TypeToken<Map<String, Any>>() {}.type
                val jsonObject = gson.fromJson(text, typeToken) as Map<String, Any>
                val listGroupJsonArray =
                    (jsonObject["payload"] as? Map<String, Any>)?.get("listGroup") as? List<Map<String, Any>>
                val listGroup =
                    listGroupJsonArray?.map { gson.fromJson(gson.toJson(it), Group::class.java) }
                recentAdapter.getListgroup(listGroup as ArrayList<Group>)

            }
        } else if (response.event == SocketRequestType.SOCKET_REQUEST_LIST_MESSAGE) {
            Handler(Looper.getMainLooper()).post(
                Runnable {
                    val typeToken = object : TypeToken<Map<String, Any>>() {}.type
                    val jsonObject = gson.fromJson<Map<String, Any>>(text, typeToken)
                    val messageJsonArray =
                        (jsonObject["payload"] as? Map<String, Any>)?.get("listMessage") as? List<Map<String, Any>>
                    val mess =
                        messageJsonArray?.map { gson.fromJson(gson.toJson(it), Message::class.java) }

                    if (mess != null && BoxChatActivity.messageAdapter != null) {
                        BoxChatActivity.messageAdapter!!.getMessages(mess as ArrayList<Message>)
                    }
                }
            )
        } else if (response.event == SocketRequestType.SOCKET_REQUEST_CREATE_MESSAGE){
            Handler(Looper.getMainLooper()).post(
                Runnable {
                    val typeToken = object : TypeToken<Map<String, Any>>() {}.type
                    val jsonObject = gson.fromJson(text, typeToken) as Map<String, Any>
                    val listGroupJsonArray =
                        (jsonObject["payload"] as? Map<String, Any>)?.get("listGroup") as? List<Map<String, Any>>
                    val listGroup =
                        listGroupJsonArray?.map { gson.fromJson(gson.toJson(it), Group::class.java) }
                    recentAdapter.getListgroup(listGroup as ArrayList<Group>)

                    val newMessageJsonObject = (jsonObject["payload"] as? Map<String, Any>)?.get("newMessage") as? Map<String, Any>
                    val newMessage = gson.fromJson(gson.toJson(newMessageJsonObject), Message::class.java)
                    if(newMessage != null && BoxChatActivity.messageAdapter != null){
                        BoxChatActivity.messageAdapter!!.addMessage(newMessage)
                    }
                }
            )
        } else if (response.event == SocketRequestType.SOCKET_REQUEST_SEEN_LASTEST_MESSAGE){
            Handler(Looper.getMainLooper()).post(
                Runnable {
                    val typeToken = object : TypeToken<Map<String, Any>>() {}.type
                    val jsonObject = gson.fromJson(text, typeToken) as Map<String, Any>
                    val listGroupJsonArray =
                        (jsonObject["payload"] as? Map<String, Any>)?.get("listGroup") as? List<Map<String, Any>>
                    val listGroup =
                        listGroupJsonArray?.map { gson.fromJson(gson.toJson(it), Group::class.java) }
                    recentAdapter.getListgroup(listGroup as ArrayList<Group>)

                    val seenMessagesJsonArray =
                        (jsonObject["payload"] as? Map<String, Any>)?.get("seenMessages") as? List<Map<String, Any>>
                    val seenMessages =
                        seenMessagesJsonArray?.map { gson.fromJson(gson.toJson(it), Message::class.java) }
                    if(seenMessages != null && BoxChatActivity.messageAdapter != null){
                        for(i in seenMessages){
                            BoxChatActivity.messageAdapter!!.checkSeenMessage(i)
                        }
                        BoxChatActivity.messageAdapter!!.notifyDataSetChanged()
                    }
                }
            )
        }

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
        lateinit var myWebSocket: WebSocket

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): Date? {
                    json?.asString?.let { dateString ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        return dateFormat.parse(dateString)
                    }
                    return null
                }
            })
            .create()

        fun exitSocket(){
            myWebSocket.close(1000, null)
            Log.e("CLOSE","9999999999999")
        }
        internal fun sendMediaMessage(message: Message, byteString: String ) {
            val map: MutableMap<String, Any> = HashMap<String, Any>()
            map["message"] = message
            map["attachment"] = byteString
            val request: SocketRequest = SocketRequest("create_message", map)
            val stringtest = gson.toJson(request)
            Log.e("TOJSON",stringtest)
            myWebSocket.send(gson.toJson(request))
        }
    }

    fun receiveMessage(webSocket: WebSocket, groupId: String) {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map["groupId"] = groupId
        val request: SocketRequest = SocketRequest("list_message", map)
        Log.e("LISTMESS",gson.toJson(request))
        webSocket.send(gson.toJson(request))
    }

    fun getListGroup(webSocket: WebSocket, userId: String) {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map["userId"] = userId.toString()
        val request: SocketRequest = SocketRequest("list_group", map)
        webSocket.send(gson.toJson(request))
    }

    fun sendMessage(webSocket: WebSocket, message: Message) {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map["message"] = message
        val request: SocketRequest = SocketRequest("create_message", map)
        val abc =gson.toJson(request)
        webSocket.send(gson.toJson(request))
    }


    fun seenMessage(webSocket: WebSocket, messageId: String) {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map["messageId"] = messageId
        val request: SocketRequest = SocketRequest("seen_message", map)
        webSocket.send(gson.toJson(request))
    }


}
