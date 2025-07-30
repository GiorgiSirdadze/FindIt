package com.example.findit.domain.usecase.chat

import com.example.findit.domain.model.Chat
import com.example.findit.domain.repository.chat.GetAllChatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: GetAllChatsRepository
) {
    operator fun invoke(userId: String): Flow<List<Chat>> {
        return repository.getAllChatsForUser(userId)
    }
}



