package com.example.findit.presentation.screen.splash

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findit.databinding.FragmentSplashBinding
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.launchCoroutine
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun setObservers() {
        launchCoroutine {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is SplashEffect.SetLocale -> {
                        setLocale(effect.languageCode)
                    }
                    is SplashEffect.NavigateToHomeScreen ->{
                        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    is SplashEffect.NavigateToLoginScreen ->{
                        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }
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
