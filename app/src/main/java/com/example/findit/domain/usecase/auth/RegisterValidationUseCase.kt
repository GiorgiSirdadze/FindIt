package com.example.findit.domain.usecase.auth

import android.util.Patterns
import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.ValidationResult
import javax.inject.Inject

class RegisterValidationUseCase @Inject constructor() {

    operator fun invoke(registerForm: RegisterForm): ValidationResult {

        if(registerForm.phone.toIntOrNull()!=null && registerForm.phone.length!=9){
            return ValidationResult.Error("Invalid phone number")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(registerForm.email).matches()) {
            return ValidationResult.Error("Invalid email address")
        }

        if (registerForm.password.length < 6) {
            return ValidationResult.Error("Password must be at least 6 characters")
        }

        if ( registerForm.password != registerForm.confirmPassword) {
            return ValidationResult.Error("Passwords do not match")
        }

        return ValidationResult.Success
    }


}