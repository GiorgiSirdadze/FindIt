package com.example.findit.presentation.screen.home

import com.example.findit.presentation.model.PostPresentation

data class HomeScreenUiState(
    val posts: List<PostPresentation> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "",
    val selectedFilters: List<String> = emptyList(),
    val error: String? = null
)