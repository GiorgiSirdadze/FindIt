package com.example.findit.presentation.screen.register

data class RegisterState(
    val error : String? = null,
    val isLoading : Boolean = false,
    val btnEnabled : Boolean = false
)