package com.example.findit.presentation.screen.post

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.findit.R
import com.example.findit.databinding.FragmentPostBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.loadImage
import com.example.findit.presentation.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {
    private val viewModel: PostViewModel by viewModels()
    private val args: PostFragmentArgs by navArgs()

    override fun setData() {
        val postId = args.postId
        viewModel.onEvent(PostEvent.LoadPost(postId))
    }


    override fun setListeners() {
        with(binding){
            tvPhoneNumber.setOnClickListener {
                val phoneNumber = tvPhoneNumber.text.toString()
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse(getString(R.string.tel, phoneNumber))
                }
                startActivity(intent)
            }
            btnViewLocation.setOnClickListener {
                viewModel.onEvent(PostEvent.ViewLocation)
            }
            btnContactUser.setOnClickListener{

                    val receiverId = viewModel.state.value.post?.userId
                    if (receiverId != null) {
                        val action = PostFragmentDirections.actionPostFragmentToChatFragment(receiverId)
                        findNavController().navigate(action)
                    } else {
                        root.showSnackBar(getString(R.string.user_not_loaded))
                    }

            }
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest { state ->

                state.post?.let {
                    with(binding){
                        ivPostImage.loadImage(it.imageUrl,R.drawable.postdefault)
                        tvPostTimestamp.text = it.timestamp
                        tvPostDescription.text = it.description
                    }
                }

                state.user?.let {
                    with(binding){
                        tvPhoneNumber.text = it.phone
                        tvUserFullname.text = getString(R.string.fullname, it.name, it.surname)
                        tvUserEmail.text = it.email
                        ivProfilePicture.loadImage(it.profileImageUrl,R.drawable.ic_my_profile)
                    }
                }

                state.error?.let {
                    binding.root.showSnackBar(it)
                }
            }
        }
        launchCoroutine {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is PostEffect.NavigateToViewLocation -> navigateToMap()
                }
            }
        }
    }

    private fun navigateToMap(){
        val postId = args.postId
        val action = PostFragmentDirections.actionPostFragmentToViewLocationFragment2(postId)
        findNavController().navigate(action)
    }
}