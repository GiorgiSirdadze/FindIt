package com.example.findit.domain.repository.post

import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.resource.Resource
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    suspend fun getPosts(): Flow<Resource<List<PostDomain>>>
    suspend fun searchPosts(query: String): Flow<Resource<List<PostDomain>>>
    suspend fun getPostById(postId: String): Flow<Resource<PostDomain>>
    suspend fun getPostsByUserID(userId: String): Flow<Resource<List<PostDomain>>>
    suspend fun deletePost(postId: String): Flow<Resource<Boolean>>
}