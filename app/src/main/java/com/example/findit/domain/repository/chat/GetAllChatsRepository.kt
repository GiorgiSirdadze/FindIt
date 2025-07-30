package com.example.findit.domain.repository.chat

import com.example.findit.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface GetAllChatsRepository {
    fun getAllChatsForUser(userId: String): Flow<List<Chat>>
}