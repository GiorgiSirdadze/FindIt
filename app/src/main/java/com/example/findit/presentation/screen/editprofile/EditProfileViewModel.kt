package com.example.findit.presentation.screen.editprofile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.model.UserProfile
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.ValidationResult
import com.example.findit.domain.usecase.user.EditProfileValidationUseCase
import com.example.findit.domain.usecase.auth.GetCurrentUserIdUseCase
import com.example.findit.domain.usecase.user.GetProfileUseCase
import com.example.findit.domain.usecase.user.UpdateProfileImageUrlUseCase
import com.example.findit.domain.usecase.user.UpdateProfileUseCase
import com.example.findit.domain.usecase.user.UploadProfilePhotoUseCase
import com.example.findit.presentation.mappper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase : GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadProfilePhotoUseCase: UploadProfilePhotoUseCase,
    private val updateProfileImageUrlUseCase: UpdateProfileImageUrlUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val validationUseCase: EditProfileValidationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state

    private val _effect = MutableSharedFlow<EditProfileEffect>()
    val effect: SharedFlow<EditProfileEffect> = _effect

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.LoadProfile -> loadProfile()
            is EditProfileEvent.OnSaveClicked -> saveProfile()
            is EditProfileEvent.OnProfileImageUriSelected -> compressImage(event.context, event.uri)
            is EditProfileEvent.OnUserProfileChanged -> _state.update { it.copy(userProfile = event.updatedProfile) }
        }
    }


    private fun loadProfile() {
        viewModelScope.launch {
            val userId = getCurrentUserIdUseCase() ?: return@launch

            getProfileUseCase(userId).collect { result ->
                when (result) {
                    is Resource.Loader -> _state.update { it.copy(isLoading = result.isLoading) }
                    is Resource.Success -> {
                        val presentation = result.data.toPresentation()
                        _state.update { it.copy(userProfile = presentation, isLoading = false) }
                    }
                    is Resource.Error -> _effect.emit(EditProfileEffect.ShowError(result.errorMessage))
                }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val profile = state.value.userProfile ?: return@launch

            val validation = validationUseCase(profile.email, profile.phone)
            if (validation is ValidationResult.Error) {
                _effect.emit(EditProfileEffect.ShowError(validation.message))
                return@launch
            }

            val user = UserProfile(
                name = profile.name,
                surname = profile.surname,
                phone = profile.phone,
                email = profile.email,
                password = profile.password,
                profileImageUrl = profile.profileImageUrl
            )

            updateProfileUseCase(user).collect { result ->
                when (result) {
                    is Resource.Loader -> _state.update { it.copy(isLoading = result.isLoading) }
                    is Resource.Success -> _effect.emit(EditProfileEffect.ProfileSaved)
                    is Resource.Error -> _effect.emit(EditProfileEffect.ShowError(result.errorMessage))
                }
            }
        }
    }



    private fun compressImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val originalBytes = inputStream?.readBytes() ?: throw IOException("Failed to read image")
                inputStream.close()

                val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
                    ?: throw IOException("Failed to decode bitmap")
                val exif = ExifInterface(originalBytes.inputStream())
                val rotationDegrees = when (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }

                val rotatedBitmap = if (rotationDegrees != 0f) {
                    val matrix = Matrix().apply { postRotate(rotationDegrees) }
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                } else bitmap

                val outputStream = ByteArrayOutputStream()
                if (!rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)) {
                    throw IOException("Failed to compress bitmap")
                }

                val finalBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
                _state.update { it.copy(profileBitmap = finalBitmap) }

                uploadProfileImage(finalBitmap)

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(EditProfileEffect.ShowError(e.localizedMessage ?: "Image compression failed"))
            }
        }
    }


    private fun uploadProfileImage(bitmap: Bitmap) {
        viewModelScope.launch {
            uploadProfilePhotoUseCase(bitmap).collect { result ->
                when (result) {
                    is Resource.Success -> updateProfileImageUrl(result.data)
                    is Resource.Error -> _effect.emit(EditProfileEffect.ShowError(result.errorMessage))
                    is Resource.Loader -> _state.update { it.copy(isLoading = result.isLoading) }
                }
            }
        }
    }

    private fun updateProfileImageUrl(url: String) {
        viewModelScope.launch {
            updateProfileImageUrlUseCase(url).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                userProfile = currentState.userProfile?.copy(profileImageUrl = url)
                            )
                        }
                        _effect.emit(EditProfileEffect.ProfileImageUpdated(url))
                    }
                    is Resource.Error -> _effect.emit(EditProfileEffect.ShowError(result.errorMessage))
                    is Resource.Loader -> _state.update { it.copy(isLoading = result.isLoading) }
                }
            }
        }
    }
}