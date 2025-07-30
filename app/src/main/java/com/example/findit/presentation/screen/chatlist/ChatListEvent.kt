package com.example.findit.presentation.screen.chatlist

sealed class ChatListEvent {
    data object LoadChats : ChatListEvent()
    data class ChatClicked(val otherUserId: String) : ChatListEvent()
}

