package com.everfrost.remak_compose.viewModel.account.resetPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isValidEmail = MutableStateFlow(false)
    val isValidEmail: StateFlow<Boolean> = _isValidEmail

    private val _isEmailSendSuccess = MutableStateFlow<Boolean?>(null)
    val isEmailSendSuccess: StateFlow<Boolean?> = _isEmailSendSuccess

    private val _isVerifySuccess = MutableStateFlow<Boolean?>(null)
    val isVerifySuccess: StateFlow<Boolean?> = _isVerifySuccess

    private val _isResetSuccess = MutableStateFlow(false)
    val isResetSuccess: StateFlow<Boolean> = _isResetSuccess

    private val _isLengthValid = MutableStateFlow(false)
    val isLengthValid: StateFlow<Boolean> = _isLengthValid
    private val _isContainNumber = MutableStateFlow(false)
    val isContainNumber: StateFlow<Boolean> = _isContainNumber
    private val _isContainEnglish = MutableStateFlow(false)
    val isContainEnglish: StateFlow<Boolean> = _isContainEnglish
    private val _isPasswordValid = MutableStateFlow(false)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid


    fun getResetPasswordCode() {
        Log.d("ResetPasswordViewModel", "getResetPasswordCode: ${_email.value}")
        viewModelScope.launch {
            val response = accountRepository.resetPasswordCode(_email.value)
            if (response is APIResponse.Success) {
                _isEmailSendSuccess.value = true
            } else {
                _isEmailSendSuccess.value = false
                Log.d("ResetPasswordViewModel", "getResetPasswordCode: ${response.message}")
            }
        }
    }

    fun checkVerifyResetCode(code: String) {
        Log.d("ResetPasswordViewModel", "checkVerifyResetCode: ${_email.value}")
        viewModelScope.launch {
            val response = accountRepository.checkVerifyResetCode(code, _email.value)
            if (response is APIResponse.Success) {
                _isVerifySuccess.value = true
            } else {
                _isVerifySuccess.value = false
                Log.d("ResetPasswordViewModel", "checkVerifyResetCode: ${response.message}")
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            val response = accountRepository.resetPassword(_email.value, _password.value)
            if (response is APIResponse.Success) {
                _isResetSuccess.value = true
            }
        }
    }

    fun checkPassword(password: String) {
        _isLengthValid.value = password.length >= 9
        _isContainNumber.value = password.matches(".*\\d.*".toRegex())
        _isContainEnglish.value = password.matches(".*[a-zA-Z].*".toRegex())
        checkPasswordValid()
    }

    private fun checkPasswordValid() {
        _isPasswordValid.value =
            _isLengthValid.value && _isContainNumber.value && _isContainEnglish.value
    }


    fun checkIsValidEmail() {
        _isValidEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setIsEmailSendSuccess(value: Boolean?) {
        _isEmailSendSuccess.value = value
    }

    fun setIsVerifySuccess(value: Boolean?) {
        _isVerifySuccess.value = value
    }
}