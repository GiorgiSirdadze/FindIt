package com.example.findit.data.repository.user

import com.example.findit.data.request.ApiHelper
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.repository.user.GetProfilePictureRepository
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.mapResourceData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfilePictureRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiHelper: ApiHelper
) : GetProfilePictureRepository {

    override suspend fun getProfilePictureUrl(userid :String): Flow<Resource<String>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.USERS).document(userid).get()
        }.mapResourceData { snapshot ->
            val imageUrl = snapshot.getString(FirestoreKeys.PROFILE_IMAGE)
            imageUrl ?: ""
        }
    }
}
