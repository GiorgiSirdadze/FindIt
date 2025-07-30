package com.example.findit.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.user.GetProfilePictureUseCase
import com.example.findit.domain.usecase.user.GetUserNameUseCase
import com.example.findit.domain.usecase.settings.SetAppLanguageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setAppLanguage: SetAppLanguageUseCase,
    private val getUserNameUseCase : GetUserNameUseCase,
    private val getProfilePictureUseCase: GetProfilePictureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect

    private val uid = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    init {
        loadImage()
        loadUserName()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.ChangeLanguageClicked -> {
                changeAppLanguage()
            }
            is ProfileEvent.EditProfileClicked -> {
                navigateToEditProfile()
            }
            is ProfileEvent.LogoutClicked ->{
                logout()
            }

            ProfileEvent.MyProfileClicked ->{
                navigateToMyProfile()
            }
        }
    }

    private fun changeAppLanguage() {
        viewModelScope.launch {
            val newLang = if (Locale.getDefault().language == "en") "ka" else "en"
            setAppLanguage(newLang)
            _effect.emit(ProfileEffect.ChangeLanguage(newLang))
        }
    }

    private fun loadImage() {
        viewModelScope.launch {
            getProfilePictureUseCase(uid).collect { result ->
                when (result) {
                    is Resource.Loader -> _state.update { it.copy(isLoading = result.isLoading) }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                profileImageUrl = result.data,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun loadUserName() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val name = getUserNameUseCase(uid)
                _state.update {
                    it.copy(
                        userName = name,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }



    private fun logout() {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            _effect.emit(ProfileEffect.NavigateToLogin)
        }
    }

    private fun navigateToEditProfile() {
        viewModelScope.launch {
            _effect.emit(ProfileEffect.NavigateToEditProfile)
        }
    }
    private fun navigateToMyProfile() {
        viewModelScope.launch {
            _effect.emit(ProfileEffect.NavigateToMyProfile)
        }
    }
}
