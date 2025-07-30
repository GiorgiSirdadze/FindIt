package com.example.findit.domain.repository.chat

import com.example.findit.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface GetMessagesRepository {
    fun getMessages(chatId: String): Flow<List<ChatMessage>>
}