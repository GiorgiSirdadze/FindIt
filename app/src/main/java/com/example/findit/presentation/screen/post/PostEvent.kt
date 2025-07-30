package com.example.findit.presentation.screen.post

sealed class PostEvent {
    data  class LoadPost(val postId: String) : PostEvent()
    data object ClearError : PostEvent()
    data object ViewLocation : PostEvent()
}