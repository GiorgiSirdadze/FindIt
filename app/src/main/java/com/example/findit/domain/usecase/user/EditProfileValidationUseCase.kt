package com.example.findit.domain.usecase.user

import android.util.Patterns
import com.example.findit.domain.resource.ValidationResult
import javax.inject.Inject

class EditProfileValidationUseCase @Inject constructor() {

    operator fun invoke(email: String, phone: String): ValidationResult {
        if (phone.toIntOrNull() != null && phone.length != 9) {
            return ValidationResult.Error("Invalid phone number")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult.Error("Invalid email address")
        }

        return ValidationResult.Success
    }
}
