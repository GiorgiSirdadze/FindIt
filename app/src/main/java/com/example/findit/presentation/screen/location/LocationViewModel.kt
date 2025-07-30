package com.example.findit.presentation.screen.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.post.GetPostsUseCase
import com.example.findit.presentation.mappper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getUserPostsUseCase: GetPostsUseCase
):ViewModel(){
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LocationEffect>()
    val event = _event.asSharedFlow()

    fun onEvent( event: LocationEvent){
        when(event) {
            is LocationEvent.OpenBottomSheet ->{
                viewModelScope.launch {
                    _event.emit(LocationEffect.OpenBottomSheet(event.postId, event.userName, event.description))
                }
            }
            is LocationEvent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
            is LocationEvent.SetCurrentUserLocation ->{
                _state.value = _state.value.copy(currentUserLocation = event.location)
            }
            LocationEvent.GetPosts ->{
                getUserPosts()
            }
        }
    }


    private fun  getUserPosts(){
        viewModelScope.launch {
            getUserPostsUseCase().collect{ state ->
                when(state){
                    is Resource.Error ->{
                        _state.value = _state.value.copy(error = state.errorMessage)

                    }

                    is Resource.Loader -> {
                        _state.value = _state.value.copy(isLoading = state.isLoading)
                    }

                    is Resource.Success ->{
                        _state.value = _state.value.copy(posts = state.data.map { it.toPresentation() })
                    }
                }
            }

        }
    }
}