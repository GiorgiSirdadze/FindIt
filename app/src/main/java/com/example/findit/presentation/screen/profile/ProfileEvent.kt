package com.example.findit.presentation.screen.profile

sealed interface ProfileEvent {
    data object ChangeLanguageClicked : ProfileEvent
    data object EditProfileClicked : ProfileEvent
    data object LogoutClicked : ProfileEvent
    data object MyProfileClicked : ProfileEvent
}