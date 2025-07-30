package com.example.findit.presentation.screen.marker

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.findit.databinding.FragmentMarkerBottomSheetBinding
import com.example.findit.presentation.base.BaseBottomSheetFragment

class MarkerBottomSheet : BaseBottomSheetFragment<FragmentMarkerBottomSheetBinding>(FragmentMarkerBottomSheetBinding::inflate) {
    private val args: MarkerBottomSheetArgs by navArgs()

    override fun setUp() {
        binding.tvUserName.text = args.userName
        binding.tvPostDescription.text= args.description
    }

    override fun setListeners() {
        binding.btnViewPost.setOnClickListener {
            val postId = args.postId
            val action = MarkerBottomSheetDirections.actionMarkerBottomSheetToPostFragment(postId)
            findNavController().navigate(action)
        }
    }
}