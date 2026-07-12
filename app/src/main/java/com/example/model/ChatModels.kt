package com.example.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ChatMessage {
    abstract val timestamp: Long

    @Serializable
    data class Text(
        val id: String,
        val content: String,
        val isFromMe: Boolean,
        override val timestamp: Long
    ) : ChatMessage()

    @Serializable
    data class System(
        val content: String,
        override val timestamp: Long
    ) : ChatMessage()
}

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val onlineCount: Int = 1,
    val isConnected: Boolean = false,
    val isTyping: Boolean = false,
    val roomCode: String = ""
)
