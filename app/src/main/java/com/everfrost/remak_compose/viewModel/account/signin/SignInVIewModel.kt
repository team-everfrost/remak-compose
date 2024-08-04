package com.everfrost.remak_compose.viewModel.account.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInVIewModel @Inject constructor() : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _isValidEmail = MutableStateFlow(false)
    val isValidEmail: StateFlow<Boolean> = _isValidEmail

    private val _emailError = MutableStateFlow(null)
    val emailError: StateFlow<Boolean?> = _emailError

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _passwordError = MutableStateFlow(null)
    val passwordError: StateFlow<Boolean?> = _passwordError

    fun setEmail(email: String) {
        _email.value = email
    }

    fun checkIsValidEmail() {
        _isValidEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    fun setPassword(password: String) {
        _password.value = password
    }


}