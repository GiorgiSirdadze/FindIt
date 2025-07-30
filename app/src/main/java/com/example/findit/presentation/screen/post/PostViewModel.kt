package com.example.findit.presentation.screen.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.post.GetPostByIdUseCase
import com.example.findit.domain.usecase.user.GetProfileUseCase
import com.example.findit.presentation.mappper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getUserProfileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<PostEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.LoadPost -> loadPost(event.postId)
            is PostEvent.ClearError -> clearError()
            is PostEvent.ViewLocation -> {
                viewModelScope.launch {
                    _effect.emit(PostEffect.NavigateToViewLocation)
                }
            }
        }
    }

    private fun loadPost(postId: String) {
        viewModelScope.launch {
            getPostByIdUseCase(postId).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.errorMessage, isLoading = false)
                    }

                    is Resource.Loader -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(post = result.data.toPresentation(), isLoading = false)
                        getUserProfileUseCase.invoke(result.data.userId).collect { userResult ->
                            when (userResult) {
                                is Resource.Error -> {
                                    _state.value =
                                        _state.value.copy(error = userResult.errorMessage, isLoading = false)
                                }
                                is Resource.Loader -> {
                                    _state.value = _state.value.copy(isLoading = userResult.isLoading)
                                }
                                is Resource.Success -> {
                                    _state.value = _state.value.copy(user = userResult.data, isLoading = false)
                                }
                            }
                        }

                    }
                }
            }


        }

    }
    private fun clearError(){
        _state.value =  _state.value.copy(error = null)
    }
}