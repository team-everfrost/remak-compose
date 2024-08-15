package com.everfrost.remak_compose.viewModel.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.repository.AccountRepository
import com.everfrost.remak_compose.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject
import kotlin.math.round


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _storageSize = MutableStateFlow(BigInteger.ZERO)
    val storageSize: StateFlow<BigInteger> = _storageSize

    private val _storageUsage = MutableStateFlow(0.0)
    val storageUsage: StateFlow<Double> = _storageUsage

    private val _usagePercentage = MutableStateFlow(0)
    val usagePercentage: StateFlow<Int> = _usagePercentage

    private val _isVerifySuccess = MutableStateFlow<Boolean?>(null)
    val isVerifySuccess: StateFlow<Boolean?> = _isVerifySuccess

    private val _isEmailSendSuccess = MutableStateFlow<Boolean?>(false)
    val isEmailSendSuccess: StateFlow<Boolean?> = _isEmailSendSuccess


    fun getUserData() {
        viewModelScope.launch {
            val response = accountRepository.getUserData()
            if (response is APIResponse.Success) {
                _userEmail.value = response.data!!.data.email
            }
        }
    }

    fun getStorageSize() {
        viewModelScope.launch {
            val response = accountRepository.getStorageSize()
            if (response is APIResponse.Success) {
                val storageBytesSize = response.data!!.data
                _storageSize.value = storageBytesSize.divide(BigInteger.valueOf(1024))
                    .divide(BigInteger.valueOf(1024))
                    .divide(BigInteger.valueOf(1024))
            }
        }
    }


    private fun getUsageSize(storageBytesSize: BigInteger) = viewModelScope.launch {
        val response = accountRepository.getStorageUsage()
        if (response is APIResponse.Success) {
            val usageByte = response.data!!.data
            //useageByte를 소수점 1자리수 GB단위로 변경
            _storageUsage.value =
                round((usageByte?.toDouble()!!.div(1024).div(1024).div(1024)) * 100) / 100
            //소수점 한자리까지 계산
            _usagePercentage.value =
                round(usageByte.toDouble() / storageBytesSize.toDouble() * 100).toInt()
        }
    }

    fun getWithdrawVerifyCode() {
        viewModelScope.launch {
            val response = accountRepository.withdrawCode()
            if (response is APIResponse.Success) {
                _isEmailSendSuccess.value = true
            } else {
                _isEmailSendSuccess.value = false
            }
        }
    }

    fun verifyWithdrawCode(code: String) {
        viewModelScope.launch {
            val response = accountRepository.verifyWithdrawCode(code)
            if (response is APIResponse.Success) {
                _isVerifySuccess.value = true
            } else {
                _isVerifySuccess.value = false
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            val response = accountRepository.withdraw()
            if (response is APIResponse.Success) {
            } else {
                Log.d("withdraw", response.message.toString())
            }
        }
    }


    fun openBrowser(url: String, context: Context) {
        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.black))
            .build()
        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colorSchemeParams)
            .build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    fun logout() {
        viewModelScope.launch {
            tokenRepository.deleteToken()
        }
    }

    fun setIsVerifySuccess(isSuccess: Boolean?) {
        _isVerifySuccess.value = isSuccess
    }

    fun setEmailSendSuccess(isSuccess: Boolean?) {
        _isEmailSendSuccess.value = isSuccess
    }
}