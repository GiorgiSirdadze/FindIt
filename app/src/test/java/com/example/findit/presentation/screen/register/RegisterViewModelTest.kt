package com.example.findit.presentation.screen.register

import com.example.findit.domain.resource.RegisterForm
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.resource.ValidationResult
import com.example.findit.domain.usecase.auth.RegisterUseCase
import com.example.findit.domain.usecase.auth.RegisterValidationUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var registerValidationUseCase: RegisterValidationUseCase
    private lateinit var viewModel: RegisterViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        registerUseCase = mockk()
        registerValidationUseCase = mockk()
        viewModel = RegisterViewModel(registerUseCase, registerValidationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submit register form - success`() = runTest {
        val form = RegisterForm("John", "Doe", "12345678", "john@example.com", "password", "password")

        every { registerValidationUseCase(form) } returns ValidationResult.Success
        coEvery { registerUseCase(form) } returns flowOf(Resource.Success(true))

        viewModel.onEvent(RegisterEvent.SubmitRegisterForm(form))
        advanceUntilIdle()

        assertEquals(false, viewModel.registerState.value.isLoading)
    }

    @Test
    fun `submit register form - validation error`() = runTest {
        val form = RegisterForm("", "", "", "", "", "")
        val errorMsg = "Fields are empty"
        every { registerValidationUseCase(form) } returns ValidationResult.Error(errorMsg)

        viewModel.onEvent(RegisterEvent.SubmitRegisterForm(form))
        advanceUntilIdle()

        assertEquals(errorMsg, viewModel.registerState.value.error)
    }

    @Test
    fun `validate form - button enabled`() = runTest {
        val form = RegisterForm("John", "Doe", "12345678", "john@example.com", "password", "password")

        viewModel.onEvent(RegisterEvent.ValidateRegisterForm(form))
        advanceUntilIdle()

        assertTrue(viewModel.registerState.value.btnEnabled)
    }

    @Test
    fun `validate form - button disabled`() = runTest {
        val form = RegisterForm("", "", "", "", "", "")

        viewModel.onEvent(RegisterEvent.ValidateRegisterForm(form))
        advanceUntilIdle()

        assertFalse(viewModel.registerState.value.btnEnabled)
    }

    @Test
    fun `clear error sets error to null`() = runTest {
        val form = RegisterForm("", "", "", "", "", "")
        val errorMsg = "Fields are empty"
        every { registerValidationUseCase(form) } returns ValidationResult.Error(errorMsg)

        viewModel.onEvent(RegisterEvent.SubmitRegisterForm(form))
        advanceUntilIdle()

        viewModel.onEvent(RegisterEvent.ClearError)
        advanceUntilIdle()

        assertNull(viewModel.registerState.value.error)
    }
}
