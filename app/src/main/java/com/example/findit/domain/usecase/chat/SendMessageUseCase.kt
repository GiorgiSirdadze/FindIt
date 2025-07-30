package com.example.findit.domain.usecase.chat

import com.example.findit.domain.model.ChatMessage
import com.example.findit.domain.repository.chat.SendMessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: SendMessageRepository
) {
    suspend operator fun invoke(chatId: String, message: ChatMessage) {
        repository.sendMessage(chatId, message)
    }
}