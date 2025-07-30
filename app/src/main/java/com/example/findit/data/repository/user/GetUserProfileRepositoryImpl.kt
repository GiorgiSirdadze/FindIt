package com.example.findit.data.repository.user

import com.example.findit.data.request.ApiHelper
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.repository.user.GetUserProfileRepository
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.mapResourceData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiHelper: ApiHelper
) : GetUserProfileRepository {

    override suspend fun getUserProfile(userid :String): Flow<Resource<UserProfile>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.USERS).document(userid).get()
        }.mapResourceData { snapshot ->
            UserProfile(
                name = snapshot.getString(FirestoreKeys.NAME) ?: "",
                surname = snapshot.getString(FirestoreKeys.SURNAME) ?: "",
                phone = snapshot.getString(FirestoreKeys.PHONE) ?: "",
                email = snapshot.getString(FirestoreKeys.EMAIL) ?: "",
                password = snapshot.getString(FirestoreKeys.PASSWORD) ?: "",
                profileImageUrl = snapshot.getString(FirestoreKeys.PROFILE_IMAGE)?: ""
            )
        }
    }
}