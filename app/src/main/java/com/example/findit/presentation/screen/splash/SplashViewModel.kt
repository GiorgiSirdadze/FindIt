package com.example.findit.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.usecase.settings.GetAppLanguageUseCase
import com.example.findit.domain.usecase.auth.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect

    init {
        startSplashLogic()
    }

    private fun startSplashLogic() {
        viewModelScope.launch {
            val language = getAppLanguageUseCase()
            _effect.emit(SplashEffect.SetLocale(language))

            val user = getCurrentUserIdUseCase()
            if (user == null) {
                _effect.emit(SplashEffect.NavigateToLoginScreen)
            } else {
                _effect.emit(SplashEffect.NavigateToHomeScreen)
            }
        }
    }
}
