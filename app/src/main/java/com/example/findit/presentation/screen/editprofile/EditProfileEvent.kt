package com.example.findit.presentation.screen.editprofile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.findit.presentation.model.UserProfilePresentation

sealed interface EditProfileEvent {
    data object LoadProfile : EditProfileEvent
    data object OnSaveClicked : EditProfileEvent
    data class OnProfileImageUriSelected(val context: Context, val uri: Uri) : EditProfileEvent
    data class OnUserProfileChanged(val updatedProfile: UserProfilePresentation) : EditProfileEvent
}