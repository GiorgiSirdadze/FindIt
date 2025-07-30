package com.example.findit.presentation.screen.chatlist

import com.example.findit.domain.model.Chat

data class ChatListState(
    val chats: List<Chat> = emptyList(),
    val userNames: Map<String, Pair<String, String?>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

