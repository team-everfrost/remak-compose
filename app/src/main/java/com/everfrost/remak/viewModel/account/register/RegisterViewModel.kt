package com.everfrost.remak.viewModel.account.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.account.SignInModel
import com.everfrost.remak.repository.AccountRepository
import com.everfrost.remak.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _privacyAgree = MutableStateFlow(false)
    val privacyAgree = _privacyAgree
    private val _serviceAgree = MutableStateFlow(false)
    val serviceAgree = _serviceAgree
    private val _allAgree = MutableStateFlow(false)
    val allAgree = _allAgree

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _isValidEmail = MutableStateFlow(false)
    val isValidEmail: StateFlow<Boolean> = _isValidEmail

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isDuplicateEmail = MutableStateFlow(false)
    val isDuplicateEmail: StateFlow<Boolean> = _isDuplicateEmail
    private val _isVerifySuccess = MutableStateFlow<Boolean?>(null)
    val isVerifySuccess: StateFlow<Boolean?> = _isVerifySuccess
    private val _isEmailSendSuccess = MutableStateFlow(false)
    val isEmailSendSuccess: StateFlow<Boolean> = _isEmailSendSuccess

    private val _isLengthValid = MutableStateFlow(false)
    val isLengthValid: StateFlow<Boolean> = _isLengthValid
    private val _isContainNumber = MutableStateFlow(false)
    val isContainNumber: StateFlow<Boolean> = _isContainNumber
    private val _isContainEnglish = MutableStateFlow(false)
    val isContainEnglish: StateFlow<Boolean> = _isContainEnglish
    private val _isPasswordValid = MutableStateFlow(false)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess


    //이메일 전송 후 확인코드 받는 로직
    fun getVerifyCode() = viewModelScope.launch {
        val response = accountRepository.getVerifyCode(_email.value)
        if (response is APIResponse.Success) {
            _isDuplicateEmail.value = false
            _isEmailSendSuccess.value = true
        } else {
            _isDuplicateEmail.value = true
        }
    }

    //확인코드 입력 후 검증받는 로직
    fun checkVerifyCode(signupCode: String) = viewModelScope.launch {
        val response =
            accountRepository.checkVerifyCode(signupCode, _email.value)
        if (response is APIResponse.Success) {
            _isVerifySuccess.value = true
        } else {
            _isVerifySuccess.value = false
        }
    }

    fun register() {
        viewModelScope.launch {
            Log.d("RegisterViewModel", "register: ${_email.value} ${_password.value}")
            val response = accountRepository.signUp(_email.value, _password.value)
            if (response is APIResponse.Success) {
                val token = response.data!!.data!!.accessToken
                tokenRepository.saveToken(SignInModel.TokenData(token))
                _registerSuccess.value = true
            } else {
                Log.d("RegisterViewModel", "register: ${response.message} ${response.errorCode}")
            }
        }
    }

    fun resetVerifySuccess() {
        _isVerifySuccess.value = null
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun checkIsValidEmail() {
        _isValidEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    fun toggleAllAgree() {
        _allAgree.value = !_allAgree.value
        _privacyAgree.value = _allAgree.value
        _serviceAgree.value = _allAgree.value
    }

    fun togglePrivacyAgree() {
        _privacyAgree.value = !_privacyAgree.value
        checkAllAgree()
    }

    fun toggleServiceAgree() {
        _serviceAgree.value = !_serviceAgree.value
        checkAllAgree()
    }

    private fun checkAllAgree() {
        _allAgree.value = _privacyAgree.value && _serviceAgree.value
    }

    fun setIsDuplicateEmail(isDuplicate: Boolean) {
        _isDuplicateEmail.value = isDuplicate
    }

    fun setIsEmailSendSuccess(isSuccess: Boolean) {
        _isEmailSendSuccess.value = isSuccess
    }

    fun setIsVerifySuccess(isSuccess: Boolean?) {
        _isVerifySuccess.value = isSuccess
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

    fun setPassword(password: String) {
        _password.value = password
    }

}