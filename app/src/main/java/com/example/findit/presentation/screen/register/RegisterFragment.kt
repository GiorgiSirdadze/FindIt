package com.example.findit.presentation.screen.register

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findit.R
import com.example.findit.databinding.FragmentRegisterBinding
import com.example.findit.domain.resource.RegisterForm
import com.example.findit.presentation.base.BaseFragment
import com.example.findit.presentation.extension.hideKeyboard
import com.example.findit.presentation.extension.launchCoroutine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun setUp() {
        setupLoginNavigationText()
        addListeners()
    }


    override fun setListeners() {
        binding.btnSignUp.setOnClickListener {
            it.hideKeyboard()
            handleRegistrationClick()
        }
    }

    override fun setObservers() {
        launchCoroutine {
            viewModel.registerState.collectLatest { state ->
                with(binding) {
                    progressBar.isVisible = state.isLoading
                    btnSignUp.isEnabled = !state.isLoading && state.btnEnabled
                    btnSignUp.isClickable = !state.isLoading && state.btnEnabled
                    errorText.isGone = true
                    state.error?.let {
                        errorText.isGone = false
                        errorText.text = state.error
                    }
                }
            }
        }

        launchCoroutine {
            viewModel.registerEvent.collectLatest { event ->
                when (event) {
                    RegisterUiEvent.NavigateToLoginScreen -> {
                        navigateToLoginScreen()
                    }
                    RegisterUiEvent.NavigateToHomeScreen -> {
                        navigateToHomeScreen()
                    }
                }
            }
        }

    }

    private fun handleRegistrationClick() {
        with(binding) {
            val registerForm = RegisterForm(
                firstName = etFirstName.text.toString().trim(),
                lastName = etLastName.text.toString().trim(),
                phone = etPhone.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                confirmPassword = etConfirmPassword.text.toString().trim()
            )

            viewModel.onEvent(RegisterEvent.SubmitRegisterForm(registerForm))
        }
    }

    private fun addListeners() {
        binding.etEmail.addTextChangedListener {
            addFieldsListener()
        }
        binding.etPassword.addTextChangedListener {
            addFieldsListener()
        }
        binding.etConfirmPassword.addTextChangedListener {
            addFieldsListener()
        }
        binding.etFirstName.addTextChangedListener {
            addFieldsListener()
        }
        binding.etLastName.addTextChangedListener {
            addFieldsListener()
        }
        binding.etPhone.addTextChangedListener {
            addFieldsListener()
        }
    }

    private fun addFieldsListener() {
        val registerForm = RegisterForm(
            firstName = binding.etFirstName.text.toString().trim(),
            lastName = binding.etLastName.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim(),
            email = binding.etEmail.text.toString().trim(),
            password = binding.etPassword.text.toString().trim(),
            confirmPassword = binding.etConfirmPassword.text.toString().trim()
        )
        viewModel.onEvent(RegisterEvent.ValidateRegisterForm(registerForm))
    }

    private fun navigateToLoginScreen() {
        findNavController().popBackStack()
    }

    private fun navigateToHomeScreen() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun setupLoginNavigationText() {
        val loginText = binding.tvLoginNav

        val fullText = getString(R.string.already_have_an_account_log_in)
        val targetText = getString(R.string.log_in)
        val spannableString = SpannableString(fullText)

        val startIndex = fullText.indexOf(targetText)
        val endIndex = startIndex + targetText.length

        if (startIndex == -1) return

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                viewModel.onEvent(RegisterEvent.NavigateToLoginScreen)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(requireContext(), R.color.holo_blue_dark)
            }
        }

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        loginText.text = spannableString
        loginText.movementMethod = LinkMovementMethod.getInstance()
        loginText.highlightColor = Color.TRANSPARENT
    }


}

