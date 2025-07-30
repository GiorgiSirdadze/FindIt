package com.example.findit.presentation.screen.login

sealed interface LoginUiEvent {
    data object NavigateToRegisterScreen : LoginUiEvent
    data object NavigateToHomeScreen : LoginUiEvent
}