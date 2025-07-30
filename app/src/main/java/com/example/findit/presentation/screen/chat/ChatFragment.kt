package com.example.findit.presentation.screen.chat

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findit.R
import com.example.findit.databinding.FragmentChatBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.loadImage
import com.example.findit.presentation.extension.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val viewModel: ChatViewModel by viewModels()
    private val adapter by lazy {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        MessageAdapter(currentUserId)
    }

    override fun setUp() {
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = this@ChatFragment.adapter
        }
    }

    override fun setListeners() {
        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString()
            if (text.isNotBlank()) {
                viewModel.onEvent(ChatEvent.SendMessage(text))
                binding.messageInput.text?.clear()
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest { state ->
                adapter.submitList(state.messages.toList()) {
                    binding.messagesRecyclerView.scrollToPosition(state.messages.size - 1)
                }

                binding.textFullName.text = state.userName
                state.profileImageUrl?.let { imageUrl ->
                    binding.imageProfile.loadImage(
                        url = imageUrl,
                        placeHolder = R.drawable.ic_my_profile
                    )
                }

                binding.progressBar.isVisible = state.isLoading
            }
        }

        launchCoroutine {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is ChatEffect.MessageSent -> {
                        binding.messagesRecyclerView.scrollToPosition(viewModel.state.value.messages.size - 1)
                    }
                    is ChatEffect.ShowError -> {
                        binding.root.showSnackBar(effect.message)
                    }
                }
            }
        }
    }
}

