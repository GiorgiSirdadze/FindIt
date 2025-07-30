package com.example.findit.presentation.screen.addpost

sealed interface AddPostUiEvent {
    data object OpenImageOptions : AddPostUiEvent
    data object OpenLocation : AddPostUiEvent
    data object AddPost : AddPostUiEvent
    data object LocationAdded : AddPostUiEvent
}