package com.example.findit.presentation.screen.add_image_dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.findit.databinding.FragmentImageOptionDialogBinding
import com.example.findit.presentation.base.BaseBottomSheetFragment
import com.example.findit.presentation.model.ImagePickOption


class ImageOptionDialog : BaseBottomSheetFragment<FragmentImageOptionDialogBinding>(FragmentImageOptionDialogBinding::inflate) {

    override fun setListeners() {
        with(binding) {
            cameraOption.setOnClickListener {
                setResult(ImagePickOption.CAMERA)
            }

            galleryOption.setOnClickListener {
                setResult(ImagePickOption.GALLERY)
            }

            cancelOption.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setResult(option: ImagePickOption) {
        setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to option.name))
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "image_option_request"
        const val RESULT_KEY = "selected_option"
    }
}