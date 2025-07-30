package com.example.findit.presentation.screen.editprofile

sealed interface EditProfileEffect {
    data object ProfileSaved : EditProfileEffect
    data class ShowError(val message: String) : EditProfileEffect
    data class ProfileImageUpdated(val url: String) : EditProfileEffect
}