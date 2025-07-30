package com.example.findit.data.repository.chat

import com.example.findit.data.extension.getOtherUserId
import com.example.findit.data.mapper.toMap
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.ChatMessage
import com.example.findit.domain.repository.chat.SendMessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class SendMessageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : SendMessageRepository {

    override suspend fun sendMessage(chatId: String, message: ChatMessage) {
        val chatRef = firestore.collection(FirestoreKeys.CHATS).document(chatId)

        chatRef.collection(FirestoreKeys.MESSAGES)
            .document(message.id)
            .set(message.toMap())

        chatRef.set(
            mapOf(
                FirestoreKeys.PARTICIPANT_IDS to listOf(message.senderId, chatId.getOtherUserId(message.senderId)),
                FirestoreKeys.LAST_MESSAGE to message.message,
                FirestoreKeys.LAST_TIMESTAMP to message.timestamp
            ), SetOptions.merge()
        )
    }
}

