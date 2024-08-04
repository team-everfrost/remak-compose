package com.everfrost.remak_compose.view.account.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.red1
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.account.widget.textfield.AccountTextField
import com.everfrost.remak_compose.view.common.BackTitleAppBar
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
    val emailError by viewModel.emailError.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = "로그인"
            )
        }
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
                        .customHeightBasedOnWidth(0.17f)
                        .background(white),
                    isError = !isValidEmail,
                    placeholder = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isEnable = if (emailError == true) false else true
                )
                if (emailError == true) {
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
                if (true) {
                    AccountTextField(
                        value = password,
                        onValueChange = {
                            viewModel.setPassword(it)
                        },
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .fillMaxWidth()
                            .customHeightBasedOnWidth(0.17f)
                            .background(white),
                        isError = false,
                        placeholder = "비밀번호를 입력해주세요",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isPassword = true
                    )

                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .fillMaxWidth()
                            .customHeightBasedOnWidth(0.17f)
                            .background(white),
                    )
                }

                if (true) {
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

                PrimaryButton(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth()
                        .customHeightBasedOnWidth(0.17f),
                    onClick = { },
                    isEnable = true,
                    text = "확인"
                )


            }
            // SignInScreen
        }
    }


}


