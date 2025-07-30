package com.example.findit.presentation.screen.view_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.post.GetPostByIdUseCase
import com.example.findit.presentation.mappper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewLocationViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViewLocationState())
    val state = _state.asStateFlow()


    fun onEvent(event: ViewLocationEvent) {
        when (event) {
            is ViewLocationEvent.GetPost -> {
                getPost(event.postId)
            }

            is ViewLocationEvent.SetCurrentUserLocation ->{
                _state.value = _state.value.copy(currentUserLocation = event.location)
            }

            is ViewLocationEvent.ClearError ->{
                clearError()
            }
        }
    }

    private fun getPost(postId: String) {
        viewModelScope.launch {
            getPostByIdUseCase(postId).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = ViewLocationState(error = result.errorMessage, isLoading = false)
                    }
                    is Resource.Loader -> {
                        _state.value = ViewLocationState(isLoading = result.isLoading)
                    }
                    is Resource.Success -> {
                        val postPresentation = result.data.toPresentation()
                        _state.value = _state.value.copy(
                            postLocation = postPresentation.location,
                            type = postPresentation.postType,
                            isLoading = false
                        )
                    }
                }

            }

        }
    }
    private fun clearError(){
        _state.value = _state.value.copy(error = null)
    }
}