package com.example.findit.presentation.screen.editprofile

import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findit.R
import com.example.findit.databinding.FragmentEditProfileBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.loadImage
import com.example.findit.presentation.extension.showSnackBar
import com.example.findit.presentation.model.ImagePickOption
import com.example.findit.presentation.screen.add_image_dialog.ImageOptionDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

    private var tempCameraUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.onEvent(EditProfileEvent.OnProfileImageUriSelected(requireContext(), it))
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempCameraUri?.let {
                    viewModel.onEvent(EditProfileEvent.OnProfileImageUriSelected(requireContext(), it))
                    tempCameraUri = null
                }
            }
        }

    override fun setUp() {
        viewModel.onEvent(EditProfileEvent.LoadProfile)

        parentFragmentManager.setFragmentResultListener(
            ImageOptionDialog.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val selected = result.getString(ImageOptionDialog.RESULT_KEY)
            val option = selected?.let { ImagePickOption.valueOf(it) }
            when (option) {
                ImagePickOption.CAMERA -> openCamera()
                ImagePickOption.GALLERY -> openGallery()
                null -> {}
            }
        }
    }


    override fun setListeners() {
        with(binding) {
            btnSave.setOnClickListener {
                val updatedProfile = viewModel.state.value.userProfile?.copy(
                    name = etName.text.toString(),
                    surname = etSurname.text.toString(),
                    phone = etPhone.text.toString(),
                    email = etEmail.text.toString(),
                )
                updatedProfile?.let {
                    viewModel.onEvent(EditProfileEvent.OnUserProfileChanged(it))
                    viewModel.onEvent(EditProfileEvent.OnSaveClicked)
                }
            }

            imageProfile.setOnClickListener {
                openOptionsDialog()
            }
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest { state ->
                with(binding) {
                    progressBar.isGone = !state.isLoading
                    btnSave.isEnabled = !state.isLoading
                    state.profileBitmap?.let {
                        imageProfile.setImageBitmap(it)
                    } ?: state.userProfile?.profileImageUrl?.takeIf { it.isNotEmpty() }?.let {
                        imageProfile.loadImage(it, R.drawable.ic_my_profile)
                    }

                    state.userProfile?.let { profile ->
                        if (etName.text.toString() != profile.name) etName.setText(profile.name)
                        if (etSurname.text.toString() != profile.surname) etSurname.setText(profile.surname)
                        if (etPhone.text.toString() != profile.phone) etPhone.setText(profile.phone)
                        if (etEmail.text.toString() != profile.email) etEmail.setText(profile.email)
                    }
                }
            }
        }

        launchCoroutine {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is EditProfileEffect.ProfileSaved -> {
                        binding.tvErrorMessage.text = ""
                        binding.root.showSnackBar("Profile saved successfully!")
                        navigateToProfile()
                    }
                    is EditProfileEffect.ProfileImageUpdated -> {
                        binding.root.showSnackBar("Image updated!")
                    }
                    is EditProfileEffect.ShowError -> {
                        binding.tvErrorMessage.text = effect.message
                    }
                }
            }
        }
    }


    private fun openGallery() {
        galleryLauncher.launch(getString(R.string.image))
    }

    private fun openCamera() {
        getTempFileUri().let { uri ->
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }
    private fun getTempFileUri(): Uri {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(getString(R.string.img, System.currentTimeMillis().toString()),
            getString(
                R.string.jpg
            ), storageDir)
        return FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.provider, requireContext().packageName),
            file
        )
    }

    private fun openOptionsDialog() {
        val action = EditProfileFragmentDirections.actionEditProfileFragmentToImageOptionDialog()
        findNavController().navigate(action)
    }

    private fun navigateToProfile(){
        val action = EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
        findNavController().navigate(action)
    }
}
