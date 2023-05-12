package com.example.ae_chat_sdk.data.socket

class SocketRequestType {
    companion object {
        const val REGISTER_SOCKET_SESSION_USER = "register_session_user"
        const val SOCKET_REQUEST_ERROR = "error"
        const val SOCKET_REQUEST_CREATE_GROUP = "create_group"
        const val SOCKET_REQUEST_UPDATE_GROUP = "update_group"
        const val SOCKET_REQUEST_MUTE_GROUP = "mute_group"
        const val SOCKET_REQUEST_UN_MUTE_GROUP = "unmute_group"
        const val SOCKET_REQUEST_PIN_GROUP = "pin_group"
        const val SOCKET_REQUEST_UN_PIN_GROUP = "unpin_group"
        const val SOCKET_REQUEST_LEAVE_GROUP = "leave_group"
        const val SOCKET_REQUEST_DELETE_GROUP = "delete_group"
        const val SOCKET_REQUEST_LIST_GROUP = "list_group"
        const val SOCKET_REQUEST_CHECK_GROUP = "check_group"
        const val SOCKET_REQUEST_DELETE_CONVERSATION = "delete_conversation"
        const val SOCKET_REQUEST_CREATE_MESSAGE = "create_message"
        const val SOCKET_REQUEST_UPDATE_MESSAGE = "update_message"
        const val SOCKET_REQUEST_LIST_MESSAGE = "list_message"
        const val SOCKET_REQUEST_DELETE_MESSAGE = "delete_message"
        const val SOCKET_REQUEST_REACTION_MESSAGE = "reaction_message"
        const val SOCKET_REQUEST_LIST_REACTION_MESSAGE = "list_reaction_message"
        const val SOCKET_REQUEST_SEEN_LASTEST_MESSAGE = "seen_message"
        const val SOCKET_REQUEST_CREATE_GROUP_SUCCESSFUL = "created_group_successful"
    }
}
