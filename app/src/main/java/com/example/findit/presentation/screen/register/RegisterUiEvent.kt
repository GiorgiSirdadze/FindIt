package com.example.findit.presentation.screen.register

sealed interface RegisterUiEvent {
    data object NavigateToLoginScreen : RegisterUiEvent
    data object NavigateToHomeScreen : RegisterUiEvent
}