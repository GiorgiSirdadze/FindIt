package com.example.findit.data.repository.post

import com.example.findit.data.mapper.fromFirestoreMap
import com.example.findit.data.mapper.toDomain
import com.example.findit.data.request.ApiHelper
import com.example.findit.data.util.FirestoreKeys
import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.repository.post.PostsRepository
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.mapResourceData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiHelper: ApiHelper
) : PostsRepository {

    override suspend fun getPosts(): Flow<Resource<List<PostDomain>>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.POSTS).get()
        }.mapResourceData { result ->
            result.documents.mapNotNull { doc ->
                runCatching { doc.data?.fromFirestoreMap()?.toDomain() }.getOrNull()
            }
        }
    }

    override suspend fun searchPosts(query: String): Flow<Resource<List<PostDomain>>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.POSTS).get()
        }.mapResourceData { result ->
            result.documents.mapNotNull { doc ->
                runCatching { doc.data?.fromFirestoreMap()?.toDomain() }.getOrNull()
            }.filter{
                it.description.contains(query, ignoreCase = true)
            }

        }
    }

    override suspend fun getPostById(postId: String): Flow<Resource<PostDomain>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.POSTS).document(postId).get()
        }.mapResourceData { document ->
            val postDto = runCatching { document.data?.fromFirestoreMap() }.getOrNull()
            postDto?.toDomain() ?:  PostDomain()
        }
    }

    override suspend fun getPostsByUserID(userId: String): Flow<Resource<List<PostDomain>>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.POSTS)
                .whereEqualTo(FirestoreKeys.USER_ID, userId)
                .get()
        }.mapResourceData { result ->
            result.documents.mapNotNull { doc ->
                runCatching { doc.data?.fromFirestoreMap()?.toDomain() }.getOrNull()
            }
        }
    }

    override suspend fun deletePost(postId: String): Flow<Resource<Boolean>> {
        return apiHelper.safeFireBaseCall {
            firestore.collection(FirestoreKeys.POSTS)
                .document(postId)
                .delete()
        }.mapResourceData {
            true
        }
    }
}
