package com.example.findit.data.repository.chat

import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.Chat
import com.example.findit.domain.repository.chat.GetAllChatsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetAllChatsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : GetAllChatsRepository {

    override fun getAllChatsForUser(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection(FirestoreKeys.CHATS)
            .whereArrayContains(FirestoreKeys.PARTICIPANT_IDS, userId)
            .orderBy(FirestoreKeys.LAST_TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val chats = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                trySend(chats)
            }

        awaitClose { listener.remove() }
    }
}
