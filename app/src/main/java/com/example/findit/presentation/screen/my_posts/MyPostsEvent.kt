package com.example.findit.presentation.screen.my_posts

sealed class MyPostsEvent {
    data object FetchPosts : MyPostsEvent()
    data class DeletePost(val postId: String) : MyPostsEvent()
    data object ClearError : MyPostsEvent()
}