package com.example.findit.presentation.screen.location

sealed interface LocationEffect {
    data class OpenBottomSheet(val postId: String, val userName: String, val description: String) : LocationEffect
}