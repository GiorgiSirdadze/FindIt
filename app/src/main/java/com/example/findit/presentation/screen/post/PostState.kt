package com.example.findit.presentation.screen.post

import com.example.findit.domain.model.UserProfile
import com.example.findit.presentation.model.PostPresentation

data class PostState(
    val isLoading: Boolean = false,
    val post: PostPresentation? = null,
    val error: String? = null,
    val user : UserProfile? = null
)