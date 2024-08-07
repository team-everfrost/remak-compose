package com.everfrost.remak_compose.view.account.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.red1
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.account.widget.textfield.AccountTextField
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.tool.customHeightBasedOnWidth
import com.everfrost.remak_compose.viewModel.account.signin.SignInVIewModel


@ExperimentalMaterial3Api
@Composable
fun SignInScreen(
    navController: NavController
) {
    val viewModel: SignInVIewModel = hiltViewModel()
    val email by viewModel.email.collectAsState()
    val isValidEmail by viewModel.isValidEmail.collectAsState()
    val password by viewModel.password.collectAsState()
    val emailCheckState by viewModel.emailCheckState.collectAsState()
    val signInState by viewModel.signInState.collectAsState()

    // FocusRequester를 사용하여 포커스를 관리
    val passwordFocusRequester = remember { FocusRequester() }
    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = "로그인"
            )
        },
        bottomBar = {
            BottomNav(navController = navController)
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp)
            ) {
                AccountTextField(
                    value = email,
                    onValueChange = {
                        viewModel.setEmail(it)
                        viewModel.checkIsValidEmail()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(63.dp)
                        .background(white),
                    isError = !isValidEmail,
                    placeholder = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isEnable = if (emailCheckState is APIResponse.Success) {
                        false
                    } else {
                        true
                    }
                )
                if (emailCheckState is APIResponse.Error) {
                    Text(
                        text = "이메일을 다시확인해주세요",
                        modifier = Modifier.padding(top = 12.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = red1,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                if (emailCheckState is APIResponse.Success) {
                    AccountTextField(
                        value = password,
                        onValueChange = {
                            viewModel.setPassword(it)
                        },
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .fillMaxWidth()
                            .height(63.dp)

                            .background(white)
                            .focusRequester(passwordFocusRequester),
                        isError = false,
                        placeholder = "비밀번호를 입력해주세요",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isPassword = true
                    )
                    LaunchedEffect(Unit) {
                        passwordFocusRequester.requestFocus()
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .fillMaxWidth()
                            .customHeightBasedOnWidth(0.17f)
                            .background(white),
                    )
                }

                if (signInState is APIResponse.Error) {
                    Text(
                        text = "비밀번호를 확인해주세요",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            color = red1
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 28.dp)
                            .fillMaxWidth()
                            .background(white)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .height(63.dp),

                    onClick = {
                        if (emailCheckState is APIResponse.Success) {
                            viewModel.signIn(email, password)
                        } else {
                            viewModel.checkEmail(email)

                        }
                    },
                    isEnable = if (emailCheckState is APIResponse.Success && password.isNotEmpty()) {
                        true
                    } else {
                        isValidEmail
                    },
                    text = if (emailCheckState is APIResponse.Success) {
                        "로그인"
                    } else {
                        "다음으로"
                    }
                )


            }
            // SignInScreen
        }
    }


}


