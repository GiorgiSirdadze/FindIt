package com.example.findit.presentation.screen.profile

sealed interface ProfileEffect {
    data class ChangeLanguage(val languageCode: String) : ProfileEffect
    data object NavigateToLogin : ProfileEffect
    data object NavigateToMyProfile : ProfileEffect
    data object NavigateToEditProfile : ProfileEffect
}
