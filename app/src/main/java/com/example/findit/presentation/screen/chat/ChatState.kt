package com.example.findit.presentation.screen.chat

import com.example.findit.domain.model.ChatMessage
import com.example.findit.presentation.model.ChatMessagePresentation

data class ChatState(
    val messages: List<ChatMessagePresentation> = emptyList(),
    val profileImageUrl: String? = null,
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
