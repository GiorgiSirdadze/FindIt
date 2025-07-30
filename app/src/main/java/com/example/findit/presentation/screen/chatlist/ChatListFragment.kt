package com.example.findit.presentation.screen.chatlist

import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findit.R
import com.example.findit.databinding.FragmentChatListBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate) {

    private val viewModel: ChatListViewModel by viewModels()

    private val adapter: ChatListAdapter by lazy {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        ChatListAdapter(currentUserId) { otherUserId ->
            viewModel.onEvent(ChatListEvent.ChatClicked(otherUserId))
        }
    }

    override fun setUp() {
        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatsRecyclerView.adapter = adapter
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest { state ->
                adapter.submitList(state.chats)
                adapter.updateUserNames(state.userNames)
                binding.progressBar.isGone = !state.isLoading
                state.error?.let {
                    binding.root.showSnackBar(it)

                }
            }
        }

        launchCoroutine {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is ChatListEffect.NavigateToChat -> {
                        findNavController().navigate(
                            R.id.action_chatListFragment_to_chatFragment,
                            bundleOf("receiverId" to effect.receiverId)
                        )
                    }
                }
            }
        }
    }
}
