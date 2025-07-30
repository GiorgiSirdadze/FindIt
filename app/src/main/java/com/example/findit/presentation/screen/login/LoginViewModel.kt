package com.example.findit.presentation.screen.login

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.auth.LogInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LogInUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _loginEvent = MutableSharedFlow<LoginUiEvent>()
    val loginEvent: SharedFlow<LoginUiEvent> = _loginEvent

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SubmitLoginForm -> {
                handleLogin(event.email, event.password)
            }

            is LoginEvent.NavigateToRegisterScreen -> {
                handleRegisterNavigation()
            }

            is LoginEvent.ValidateLoginForm -> {
                validateLoginForm(event.email, event.password)
            }

            LoginEvent.ClearError ->{
                clearError()
            }
        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        d("LoginViewModel", "Login error: ${resource.errorMessage}")
                        _loginState.value = _loginState.value.copy(
                            error = resource.errorMessage,
                            isLoading = false
                        )
                    }

                    is Resource.Loader -> {
                        _loginState.value = _loginState.value.copy(
                            isLoading = resource.isLoading
                        )
                    }

                    is Resource.Success -> {
                        _loginState.value = _loginState.value.copy(
                            isLoading = false
                        )
                        d("LoginViewModel", "Login successful")
                        _loginEvent.emit(LoginUiEvent.NavigateToHomeScreen)
                    }
                }
            }
        }
    }

    private fun handleRegisterNavigation() {
        viewModelScope.launch {
            _loginEvent.emit(LoginUiEvent.NavigateToRegisterScreen)
        }

    }

    private fun validateLoginForm(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _loginState.value = _loginState.value.copy(
                    btnEnabled = false
                )
            } else {
                _loginState.value = _loginState.value.copy(
                    btnEnabled = true
                )
            }
        }
    }

    private fun clearError() {
        _loginState.value = _loginState.value.copy(
            error = null
        )

    }

}