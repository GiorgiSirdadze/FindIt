package com.example.findit.presentation.screen.login

import com.example.findit.domain.usecase.auth.LogInUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var loginUseCase: LogInUseCase
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `validate form - button enabled`() = runTest {
        val email = "test@example.com"
        val password = "password"

        viewModel.onEvent(LoginEvent.ValidateLoginForm(email, password))
        advanceUntilIdle()

        assertTrue(viewModel.loginState.value.btnEnabled)
    }

    @Test
    fun `validate form - button disabled`() = runTest {
        val email = ""
        val password = ""

        viewModel.onEvent(LoginEvent.ValidateLoginForm(email, password))
        advanceUntilIdle()

        assertFalse(viewModel.loginState.value.btnEnabled)
    }

}
