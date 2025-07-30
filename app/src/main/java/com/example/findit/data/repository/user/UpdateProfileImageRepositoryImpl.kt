package com.example.findit.data.repository.user

import com.example.findit.domain.repository.user.UpdateProfileImageRepository
import com.example.findit.domain.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateProfileImageRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UpdateProfileImageRepository {
    override suspend fun updateProfileImageUrl(imageUrl: String): Flow<Resource<Unit>> = flow {
        try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.collection("users").document(uid).update("profileImage", imageUrl).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update profile image URL"))
        }
    }.flowOn(Dispatchers.IO)
}
