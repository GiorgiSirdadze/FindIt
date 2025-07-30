package com.example.findit.data.repository.post

import com.example.findit.data.mapper.toDto
import com.example.findit.data.mapper.toFirestoreMap
import com.example.findit.data.request.ApiHelper
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.repository.post.UploadPostRepository
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.mapResourceData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadPostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiHelper: ApiHelper
) : UploadPostRepository {

    override suspend fun uploadPost(post: PostDomain): Flow<Resource<Boolean>> {
        return apiHelper.safeFireBaseCall {
            val docRef = firestore.collection(FirestoreKeys.POSTS).document()
            val postDto = post.toDto().copy(postId = docRef.id)
            val postData = postDto.toFirestoreMap()
            docRef.set(postData)
            firestore.collection(FirestoreKeys.POSTS)
                .document(postDto.postId)
                .set(postData)
        }.mapResourceData {
            true
        }
    }
}