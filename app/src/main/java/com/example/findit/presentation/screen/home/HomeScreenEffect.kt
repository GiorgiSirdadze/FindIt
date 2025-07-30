package com.example.findit.presentation.screen.home

sealed interface HomeScreenEffect {
    data class NavigateToPostInfo(val postId : String) : HomeScreenEffect
}