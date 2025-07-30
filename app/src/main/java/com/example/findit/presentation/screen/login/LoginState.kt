package com.example.findit.presentation.screen.login

data class LoginState(
    val error : String? = null,
    val isLoading : Boolean = false,
    val btnEnabled : Boolean = false
)