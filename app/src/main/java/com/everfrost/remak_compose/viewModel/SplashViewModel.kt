package com.everfrost.remak_compose.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.repository.TokenRepository
import com.everfrost.remak_compose.view.RemakScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val tokenRepository: TokenRepository) :
    ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> get() = _isReady

    private val _isToken = MutableStateFlow(false)
    val isToken: MutableStateFlow<Boolean> get() = _isToken

    private val _screen: MutableStateFlow<String> = MutableStateFlow("")
    val screen: MutableStateFlow<String> get() = _screen


    init {
        Log.d("SplashViewModel", "init")
        viewModelScope.launch {
            _isToken.value = tokenRepository.checkToken()
            if (_isToken.value) {
                _screen.value = RemakScreen.Main.route
            } else {
                _screen.value = RemakScreen.OnBoarding.route
            }
            _isReady.value = true
        }
    }
}