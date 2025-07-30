package com.example.findit.presentation.screen.my_posts

import com.example.findit.presentation.model.PostPresentation

data class MyPostsState (
    val posts: List<PostPresentation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)