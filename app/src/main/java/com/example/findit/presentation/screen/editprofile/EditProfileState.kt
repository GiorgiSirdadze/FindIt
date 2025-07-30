package com.example.findit.presentation.screen.editprofile

import android.graphics.Bitmap
import com.example.findit.presentation.model.UserProfilePresentation

data class EditProfileState(
    val userProfile : UserProfilePresentation? = null,
    val isLoading: Boolean = false,
    val profileBitmap: Bitmap? = null
)