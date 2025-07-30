package com.example.findit.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.ValidationResult
import com.example.findit.domain.usecase.auth.RegisterUseCase
import com.example.findit.domain.usecase.auth.RegisterValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerValidationUseCase: RegisterValidationUseCase
) :ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    private val _registerEvent = MutableSharedFlow<RegisterUiEvent>()
    val registerEvent: SharedFlow<RegisterUiEvent> = _registerEvent


    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NavigateToLoginScreen ->{
               handleLoginNavigation()
            }
            is RegisterEvent.SubmitRegisterForm ->{
                handleRegister(event.registerForm)
            }
            is RegisterEvent.ValidateRegisterForm ->{
                validateRegisterForm(event.registerForm)
            }

            RegisterEvent.ClearError -> {
                clearError()
            }
        }

    }

    private fun handleLoginNavigation(){
        viewModelScope.launch {
            _registerEvent.emit(RegisterUiEvent.NavigateToLoginScreen)
        }
    }

    private fun handleRegister(registerForm: RegisterForm) {
        viewModelScope.launch {
            when (val result = registerValidationUseCase(registerForm)) {
                is ValidationResult.Success -> {
                    registerUseCase(registerForm).collectLatest { resource ->
                        when (resource) {
                            is Resource.Error -> {
                                _registerState.value = _registerState.value.copy(error = resource.errorMessage, isLoading = false)
                            }
                            is Resource.Loader ->{
                                _registerState.value = _registerState.value.copy(isLoading = resource.isLoading)
                            }
                            is Resource.Success -> {
                                _registerState.value = _registerState.value.copy(isLoading = false)
                                _registerEvent.emit(RegisterUiEvent.NavigateToHomeScreen)
                            }

                        }
                    }
                }

                is ValidationResult.Error -> {
                    _registerState.value = _registerState.value.copy(error = result.message)
                }
            }
        }
    }

    private fun validateRegisterForm(registerForm: RegisterForm) {
        viewModelScope.launch {
            if (!registerForm.allFieldsFilled()) {
                _registerState.value = _registerState.value.copy(btnEnabled = false)
            }else {
                _registerState.value = _registerState.value.copy(btnEnabled = true)
            }

        }
    }

    private fun clearError() {
        _registerState.value = _registerState.value.copy(error = null)
    }
}