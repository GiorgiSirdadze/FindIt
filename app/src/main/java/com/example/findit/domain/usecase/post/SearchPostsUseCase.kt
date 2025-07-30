package com.example.findit.domain.usecase.post

import com.example.findit.domain.repository.post.PostsRepository
import javax.inject.Inject

class SearchPostsUseCase @Inject constructor (
    val repository: PostsRepository
){
    suspend operator fun invoke(query: String) = repository.searchPosts(query)
}