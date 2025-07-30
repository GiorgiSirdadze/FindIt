package com.example.findit.presentation.screen.chat

sealed class ChatEvent {
    data class SendMessage(val text: String) : ChatEvent()

}
