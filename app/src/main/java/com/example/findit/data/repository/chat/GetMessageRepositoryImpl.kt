package com.example.findit.data.repository.chat


import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.ChatMessage
import com.example.findit.domain.repository.chat.GetMessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetMessageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : GetMessagesRepository {

    override fun getMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = firestore.collection(FirestoreKeys.CHATS)
            .document(chatId)
            .collection(FirestoreKeys.MESSAGES)
            .orderBy(FirestoreKeys.TIMESTAMP)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

}
