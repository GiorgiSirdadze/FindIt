package com.example.findit.presentation.screen.splash

sealed interface SplashEffect {
    data class SetLocale(val languageCode: String) : SplashEffect
    data object NavigateToHomeScreen : SplashEffect
    data object NavigateToLoginScreen : SplashEffect
}