package com.example.findit.presentation.screen.my_posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.post.DeletePostByIdUseCase
import com.example.findit.domain.usecase.auth.GetCurrentUserIdUseCase
import com.example.findit.domain.usecase.post.GetPostByUserIdUseCase
import com.example.findit.presentation.mappper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostsViewModel @Inject constructor(
    private val getPostsByUserIdUseCase: GetPostByUserIdUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val deletePostByIdUseCase: DeletePostByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MyPostsState())
    val state = _state.asStateFlow()


    fun onEvent(event: MyPostsEvent) {
        when (event) {
            is MyPostsEvent.FetchPosts -> {
                getPostsByUserId()
            }

            is MyPostsEvent.DeletePost -> {
                deletePost(event.postId)
            }

            MyPostsEvent.ClearError ->{
                _state.value = _state.value.copy(
                    error = null
                )
            }
        }
    }

    private fun getPostsByUserId() {
        val userId = getCurrentUserIdUseCase() ?: return
        viewModelScope.launch {
            getPostsByUserIdUseCase(userId).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.errorMessage
                        )
                    }

                    is Resource.Loader -> {
                        _state.value = _state.value.copy(
                            isLoading = resource.isLoading
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            posts = resource.data.map { it.toPresentation() }
                        )
                    }
                }
            }
        }
    }

    private fun deletePost(postId: String) {
        viewModelScope.launch {
            deletePostByIdUseCase(postId).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.errorMessage
                        )
                    }

                    is Resource.Loader ->{
                        _state.value = _state.value.copy(
                            isLoading = resource.isLoading
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            posts = _state.value.posts.filter { it.postId != postId }
                        )

                    }
                }

            }

        }
    }
}