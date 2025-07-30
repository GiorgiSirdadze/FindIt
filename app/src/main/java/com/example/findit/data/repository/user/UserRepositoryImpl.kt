package com.example.findit.data.repository.user

import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun getUserFullName(userId: String): String {
        try {
            val snapshot = firestore.collection(FirestoreKeys.USERS).document(userId).get().await()
            val name = snapshot.getString(FirestoreKeys.NAME)
            val surname = snapshot.getString(FirestoreKeys.SURNAME)
            val fullName = "$name $surname"
            return fullName
        } catch (e: Exception) {
            return "unknown user"
        }
    }
    override suspend fun getUserProfile(userId: String): String {
        try {
            val snapshot = firestore.collection(FirestoreKeys.USERS).document(userId).get().await()
            val profileImageUrl = snapshot.getString(FirestoreKeys.PROFILE_IMAGE)
            return profileImageUrl ?: ""
        } catch (e: Exception) {
            return ""
        }
    }

}
