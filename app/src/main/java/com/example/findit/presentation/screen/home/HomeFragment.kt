package com.example.findit.presentation.screen.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findit.R
import com.example.findit.databinding.FragmentHomeBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.example.findit.presentation.screen.home.helper.PostAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate){
    private val viewModel: HomeScreenViewModel by viewModels()

    private val adapter : PostAdapter by lazy {
        PostAdapter{
            viewModel.onEvent(HomeScreenEvent.OnPostClicked(it))
        }
    }

    override fun setRecycler() {
        with(binding){
            postsRecyclerView.adapter = adapter
            searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(HomeScreenEvent.OnSearchQueryChanged(text.toString()))
            }
            filterIcon.setOnClickListener {
                showFilterPopup(it)
            }
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.state.collectLatest { state->
                with(binding) {
                    if (state.posts.isNotEmpty()) {
                        adapter.submitList(state.posts)
                        postsRecyclerView.isGone = false
                    } else {
                        postsRecyclerView.isGone = true
                    }

                    noPostsFoundText.isGone = state.posts.isNotEmpty() || state.isLoading
                    progressBar.isGone = !state.isLoading
                    state.error?.let {
                        root.showSnackBar(it)
                        viewModel.onEvent(HomeScreenEvent.ClearError)
                    }
                    welcomeText.text = getString(R.string.welcome, state.userName)

                }

            }

        }
        launchCoroutine {
            viewModel.effect.collectLatest {
                when(it){
                    is HomeScreenEffect.NavigateToPostInfo ->{
                        openPost(it.postId)
                    }
                }
            }
        }
    }

    private fun openPost(postId : String){
        val action = HomeFragmentDirections.actionHomeFragmentToPostFragment(postId)
        findNavController().navigate(action)
    }





    private fun showFilterPopup(anchor: View) {
        val popupView = layoutInflater.inflate(R.layout.filter_popup, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true).apply {
            elevation = 10f
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = popupView.measuredWidth

        popupWindow.showAsDropDown(anchor, -popupWidth + anchor.width, 10)

        val filters = mapOf(
            R.id.filter_today to "today",
            R.id.filter_week to "week",
            R.id.filter_month to "month",
            R.id.filter_lost to "lost",
            R.id.filter_found to "found"
        )

        val applyButton = popupView.findViewById<Button>(R.id.apply_filters_button)
        applyButton.setOnClickListener {
            val selectedFilters = filters.filter { (checkboxId, filter) ->
                popupView.findViewById<CheckBox>(checkboxId).isChecked
            }.values.toList()

            viewModel.onEvent(HomeScreenEvent.OnFiltersSelected(selectedFilters))

            popupWindow.dismiss()
        }
    }



}