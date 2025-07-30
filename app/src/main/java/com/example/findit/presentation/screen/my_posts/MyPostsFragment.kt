package com.example.findit.presentation.screen.my_posts

import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.example.findit.databinding.FragmentMyPostsBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.example.findit.presentation.screen.my_posts.helper.MyPostAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPostsFragment : BaseFragment<FragmentMyPostsBinding>(FragmentMyPostsBinding::inflate) {
    private val viewModel: MyPostsViewModel by viewModels()
    private val adapter: MyPostAdapter by lazy {
        MyPostAdapter {
            viewModel.onEvent(MyPostsEvent.DeletePost(it))
        }
    }

    override fun setUp() {
        viewModel.onEvent(MyPostsEvent.FetchPosts)
    }
    override fun setRecycler() {
        binding.myPostsRecycler.adapter = adapter
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collect { state ->
                adapter.submitList(state.posts)
                with(binding) {
                    progressBar.isGone = !state.isLoading
                    noPostsFoundText.isGone = state.posts.isNotEmpty() || state.isLoading
                    state.error?.let {
                        root.showSnackBar(it)
                        viewModel.onEvent(MyPostsEvent.ClearError)
                    }
                }
            }
        }
    }
}