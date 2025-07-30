package com.example.findit.presentation.screen.chat

sealed class ChatEffect {
    data object MessageSent : ChatEffect()
    data class ShowError(val message: String) : ChatEffect()
}
