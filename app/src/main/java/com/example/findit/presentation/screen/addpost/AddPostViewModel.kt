package com.example.findit.presentation.screen.addpost

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.model.LocationModel
import com.example.findit.domain.model.PostDomain
import com.example.findit.domain.model.PostType
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.auth.GetCurrentUserIdUseCase
import com.example.findit.domain.usecase.user.GetUserNameUseCase
import com.example.findit.domain.usecase.user.GetUserProfileUrlUseCase
import com.example.findit.domain.usecase.post.UploadPostPhotoUseCase
import com.example.findit.domain.usecase.post.UploadPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    @ApplicationContext private val  context: Context,
    private val uploadPostPhotoUseCase: UploadPostPhotoUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val uploadPostUseCase: UploadPostUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getUserProfileUrlUseCase: GetUserProfileUrlUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(AddPostState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddPostUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: AddPostEvent) {
        when(event) {
            is AddPostEvent.AddLocation -> {
                _state.value = _state.value.copy(location = event.latLng)
                viewModelScope.launch {
                    _event.emit(AddPostUiEvent.LocationAdded)
                }
            }
            is AddPostEvent.OpenDialog ->{
                handleOpenDialog()
            }
            is AddPostEvent.AddPost ->{
                addPost(event.type,event.description)
            }
            is AddPostEvent.ClearError -> {
                clearError()
            }

            is AddPostEvent.ImageSelected ->{
                compressImage(context ,event.uri)
            }

            is AddPostEvent.OpenMap ->{
                viewModelScope.launch {
                    _event.emit(AddPostUiEvent.OpenLocation)
                }
            }

        }
    }

    private fun compressImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
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
                _state.value = _state.value.copy(bitmap = finalBitmap)

            } catch (e: Exception) {
                _state.value = _state.value.copy(btnEnabled = false, error = e.localizedMessage)
            }
        }
    }



    private fun addPost(type : PostType, description: String){
        viewModelScope.launch {
            val latLng = state.value.location
            if(latLng==null){
                _state.value = _state.value.copy(error = "you must specify location")
                return@launch
            }
            _state.value = _state.value.copy(isLoading = true)
            state.value.bitmap?.let { uploadPostPhotoUseCase(it) }?.collectLatest { resource ->
                when(resource){
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = resource.errorMessage, isLoading = false)
                    }
                    is Resource.Loader ->{
                        _state.value = _state.value.copy(isLoading = resource.isLoading)
                    }
                    is Resource.Success ->{

                        val currentUser = getCurrentUserIdUseCase() ?: ""
                        val profilePicture = getUserProfileUrlUseCase(currentUser)
                        val fullName = getUserNameUseCase(currentUser)

                        val post = PostDomain(
                            imageUrl = resource.data,
                            description = description,
                            userId = currentUser,
                            location = LocationModel(longitude = latLng.longitude, latitude = latLng.latitude),
                            timestamp = System.currentTimeMillis(),
                            postType = type,
                            userFullName = fullName,
                            userProfilePicture = profilePicture
                        )

                        uploadPostUseCase(post).collectLatest{
                            when(it){
                                is Resource.Error ->{
                                    _state.value = _state.value.copy(error = it.errorMessage, isLoading = false)
                                }
                                is Resource.Loader ->{
                                    _state.value = _state.value.copy(isLoading = it.isLoading)
                                }
                                is Resource.Success ->{
                                    _state.value = _state.value.copy(isLoading = false)
                                    _event.emit(AddPostUiEvent.AddPost)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun handleOpenDialog() {
        viewModelScope.launch {
            _event.emit(AddPostUiEvent.OpenImageOptions)
        }
    }


    private fun clearError(){
        _state.value = _state.value.copy(error = null)
    }

}