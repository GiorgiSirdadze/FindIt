package com.example.findit.domain.repository.chat

import com.example.findit.domain.model.ChatMessage

interface SendMessageRepository {
    suspend fun sendMessage(chatId: String, message: ChatMessage)
}