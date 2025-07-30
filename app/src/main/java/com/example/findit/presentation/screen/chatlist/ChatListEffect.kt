package com.example.findit.presentation.screen.chatlist

sealed class ChatListEffect {
    data class NavigateToChat(val receiverId: String) : ChatListEffect()
}
