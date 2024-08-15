package com.everfrost.remak.viewModel.account.signin

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
class SignInVIewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _isValidEmail = MutableStateFlow(false)
    val isValidEmail: StateFlow<Boolean> = _isValidEmail

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _emailCheckState =
        MutableStateFlow<APIResponse<SignInModel.CheckEmailResponse>>(APIResponse.Empty())
    val emailCheckState: StateFlow<APIResponse<SignInModel.CheckEmailResponse>> = _emailCheckState

    private val _signInState =
        MutableStateFlow<APIResponse<SignInModel.ResponseBody>>(APIResponse.Empty())
    val signInState: StateFlow<APIResponse<SignInModel.ResponseBody>> = _signInState

    fun setEmail(email: String) {
        _email.value = email
    }

    fun checkIsValidEmail() {
        _isValidEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun checkEmail(email: String) = viewModelScope.launch {
        _emailCheckState.value = accountRepository.checkEmail(email)
        if (_emailCheckState.value is APIResponse.Error) {

        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _signInState.value = accountRepository.signIn(email, password)
        if (_signInState.value is APIResponse.Success) {
            val token = SignInModel.TokenData(
                (_signInState.value as APIResponse.Success).data!!.data!!.accessToken
            )
            tokenRepository.saveToken(token)
        }

        Log.d("token", tokenRepository.fetchToken()?.accessToken.toString());
    }


}