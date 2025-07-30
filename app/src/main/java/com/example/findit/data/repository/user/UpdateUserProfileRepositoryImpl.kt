package com.example.findit.data.repository.user

import com.example.findit.data.request.ApiHelper
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.repository.user.UpdateUserProfileRepository
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.mapResourceData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val apiHelper : ApiHelper
) : UpdateUserProfileRepository {

    override suspend fun updateUserProfile(profile: UserProfile): Flow<Resource<Boolean>> {
        val uid = auth.currentUser?.uid ?: return flow {
            emit(Resource.Error("User not logged in"))
        }
        val profileMap = mapOf(
            FirestoreKeys.NAME to profile.name,
            FirestoreKeys.SURNAME to profile.surname,
            FirestoreKeys.PHONE to profile.phone,
            FirestoreKeys.EMAIL to profile.email,
            FirestoreKeys.PASSWORD to profile.password
        )
        return apiHelper.safeFireBaseCall {
            firestore.collection("users").document(uid).update(profileMap)
        }.mapResourceData {
            true
        }
    }

}