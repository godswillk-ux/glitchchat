package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ChatMessage
import com.example.model.ChatState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun joinRoom(roomCode: String) {
        _state.update { it.copy(roomCode = roomCode, isConnected = true) }
        addSystemMessage("You joined the room $roomCode")
        
        // Simulate someone joining
        viewModelScope.launch {
            delay(2000)
            addSystemMessage("Someone joined")
            _state.update { it.copy(onlineCount = it.onlineCount + 1) }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        val newMessage = ChatMessage.Text(
            id = UUID.randomUUID().toString(),
            content = content,
            isFromMe = true,
            timestamp = System.currentTimeMillis()
        )
        
        _state.update { it.copy(messages = it.messages + newMessage) }
        
        // Simulate echo for demo purposes since no backend is provided
        simulateResponse(content)
    }

    private fun simulateResponse(userMessage: String) {
        viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(isTyping = true) }
            delay(1500)
            _state.update { it.copy(isTyping = false) }
            
            val response = ChatMessage.Text(
                id = UUID.randomUUID().toString(),
                content = "Anonymous: $userMessage",
                isFromMe = false,
                timestamp = System.currentTimeMillis()
            )
            _state.update { it.copy(messages = it.messages + response) }
        }
    }

    private fun addSystemMessage(content: String) {
        val systemMessage = ChatMessage.System(
            content = content,
            timestamp = System.currentTimeMillis()
        )
        _state.update { it.copy(messages = it.messages + systemMessage) }
    }
}
