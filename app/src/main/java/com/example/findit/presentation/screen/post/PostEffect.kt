package com.example.findit.presentation.screen.post

sealed interface PostEffect {
    data object NavigateToViewLocation : PostEffect
}