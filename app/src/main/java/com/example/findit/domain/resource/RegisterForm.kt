package com.example.findit.domain.resource

data class RegisterForm(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String,
    val confirmPassword: String
){
    fun allFieldsFilled(): Boolean{
        return firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                phone.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty()  &&
                confirmPassword.isNotEmpty()
    }
}