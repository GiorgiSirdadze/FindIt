package com.example.findit.presentation.screen.register

import com.example.findit.domain.resource.RegisterForm

sealed class RegisterEvent {
    data class SubmitRegisterForm(val registerForm: RegisterForm) : RegisterEvent()
    data object NavigateToLoginScreen : RegisterEvent()
    data class ValidateRegisterForm(val registerForm: RegisterForm) : RegisterEvent()
    data object ClearError : RegisterEvent()
}