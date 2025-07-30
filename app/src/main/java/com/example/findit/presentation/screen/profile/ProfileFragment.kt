package com.example.findit.presentation.screen.profile

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.findit.R
import com.example.findit.databinding.FragmentProfileBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.loadImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun setListeners() {
        binding.itemChangeLanguage.setOnClickListener {
            viewModel.onEvent(ProfileEvent.ChangeLanguageClicked)
        }
        binding.itemEditProfile.setOnClickListener {
            viewModel.onEvent(ProfileEvent.EditProfileClicked)
        }
        binding.buttonLogout.setOnClickListener {
            viewModel.onEvent(ProfileEvent.LogoutClicked)
        }
        binding.itemMyProfile.setOnClickListener {
            viewModel.onEvent(ProfileEvent.MyProfileClicked)
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ProfileEffect.ChangeLanguage -> setLocale(effect.languageCode)
                    ProfileEffect.NavigateToEditProfile -> {
                        val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                        findNavController().navigate(action)
                    }
                    ProfileEffect.NavigateToLogin -> findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    ProfileEffect.NavigateToMyProfile ->{
                        val action = ProfileFragmentDirections.actionProfileFragmentToMyPostsFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        launchCoroutine {
            viewModel.state.collect { state ->
                binding.textFullName.text = state.userName
                state.profileImageUrl?.let { imageUrl ->
                    binding.imageProfile.loadImage(
                        url = imageUrl,
                        placeHolder = R.drawable.ic_my_profile
                    )
                }
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        requireActivity().recreate()
    }
}
 