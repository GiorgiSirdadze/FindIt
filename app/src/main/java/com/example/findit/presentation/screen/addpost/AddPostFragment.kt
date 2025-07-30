package com.example.findit.presentation.screen.addpost

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findit.R
import com.example.findit.databinding.FragmentAddPostBinding
import com.example.findit.domain.model.PostType
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.example.findit.presentation.model.ImagePickOption
import com.example.findit.presentation.screen.add_image_dialog.ImageOptionDialog
import com.example.findit.presentation.screen.mark.MarkFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@AndroidEntryPoint
class AddPostFragment : BaseFragment<FragmentAddPostBinding>(FragmentAddPostBinding::inflate) {

    private val viewModel : AddPostViewModel by viewModels()

    private var tempCameraUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.onEvent(AddPostEvent.ImageSelected(it))
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempCameraUri?.let { uri ->
                    viewModel.onEvent(AddPostEvent.ImageSelected(uri))
                    tempCameraUri = null
                }
            }
        }
    override fun setUp() {
        parentFragmentManager.setFragmentResultListener(
            ImageOptionDialog.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val selected = result.getString(ImageOptionDialog.RESULT_KEY)
            val option = selected?.let { ImagePickOption.valueOf(it) }

            when (option) {
                ImagePickOption.CAMERA -> {
                    openCamera()
                }

                ImagePickOption.GALLERY -> {
                    openGallery()
                }
                null -> {}
            }
        }
        parentFragmentManager.setFragmentResultListener(
            MarkFragment.REQUEST_KEY,
            viewLifecycleOwner
        ){
            _,result ->
            val location = result.getParcelable<LatLng>(MarkFragment.SELECTED_LOCATION_KEY)
            location?.let {
                viewModel.onEvent(AddPostEvent.AddLocation(it))
            }
        }
    }

    override fun setListeners() {
        with(binding) {
            addPhotoId.setOnClickListener {
                viewModel.onEvent(AddPostEvent.OpenDialog)
            }

            addLocationId.setOnClickListener {
                viewModel.onEvent(AddPostEvent.OpenMap)
            }

            addPostId.setOnClickListener {
                addPost()
            }

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest{ state ->
                with(binding) {
                    photoId.setImageBitmap(state.bitmap)
                    state.bitmap?.let {
                        addPhotoId.text = getString(R.string.change_image)
                        addPhotoId.setBackgroundDrawable(requireContext().getDrawable(R.drawable.change_location_button))
                        addPhotoId.setTextColor(R.color.black)
                    }
                    photoId.isGone = state.bitmap == null
                    state.error?.let {
                        root.showSnackBar(it)
                    }
                    progressBar.isGone = !state.isLoading
                    addPostId.isEnabled = !state.isLoading
                    addLocationId.isEnabled = !state.isLoading
                    addPostId.isClickable = !state.isLoading
                    addLocationId.isClickable = !state.isLoading
                }
            }
        }

        launchCoroutine {
            viewModel.event.collectLatest{event->
                when(event){
                    is AddPostUiEvent.OpenImageOptions ->{
                        openOptionsDialog()
                    }

                    is AddPostUiEvent.OpenLocation ->{
                        navigateToMap()
                    }

                    is AddPostUiEvent.AddPost ->{
                        binding.root.showSnackBar(getString(R.string.successfully_uploaded))
                        navigateToHomeScreen()
                    }

                   is  AddPostUiEvent.LocationAdded ->{
                        with(binding) {
                            addLocationId.text = getString(R.string.change_location)
                            addLocationId.setBackgroundDrawable(requireContext().getDrawable(R.drawable.change_location_button))
                            addLocationId.setTextColor(R.color.black)
                        }
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

    private fun addPost(){
        with(binding){
            val desc = descriptionId.text.toString()
            val postType = when (binding.typeRadioGroup.checkedRadioButtonId) {
                R.id.found_radio -> PostType.FOUND
                R.id.lost_radio -> PostType.LOST
                else -> PostType.FOUND
            }
            viewModel.onEvent(AddPostEvent.AddPost(type = postType, desc))
        }
    }

    private fun openOptionsDialog(){
        val action = AddPostFragmentDirections.actionAddPostFragmentToImageOptionDialog()
        findNavController().navigate(action)
    }

    private fun navigateToHomeScreen(){
        val action = AddPostFragmentDirections.actionAddPostFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMap(){
        val action = AddPostFragmentDirections.actionAddPostFragmentToMarkFragment()
        findNavController().navigate(action)
    }

}