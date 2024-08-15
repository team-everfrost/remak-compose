package com.everfrost.remak.view.profile

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.RemakScreen
import com.everfrost.remak.view.account.widget.textfield.AccountTextField
import com.everfrost.remak.view.common.appbar.BackTitleAppBar
import com.everfrost.remak.view.common.button.PrimaryButton
import com.everfrost.remak.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak.viewModel.account.resetPassword.ResetPasswordViewModel

@Composable
fun ProfileResetPassword3Screen(
    navController: NavController,
    viewModel: ResetPasswordViewModel
) {


    val password by viewModel.password.collectAsState()
    var passwordCheck by remember { mutableStateOf("") }
    val isResetSuccess by viewModel.isResetSuccess.collectAsState()


    when {
        isResetSuccess ->
            CustomConfirmDialog(
                onDismissRequest = {
                    navController.navigate(RemakScreen.OnBoarding.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                mainTitle = "비밀번호가 변경되었습니다",
                subTitle = "다시 로그인해주세요",
                btnText = "확인"
            )
    }
    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = "비밀번호 변경"
            )
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
                Text(
                    text = "비밀번호 확인", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium
                    )
                )
                AccountTextField(
                    value = passwordCheck,
                    onValueChange = {
                        passwordCheck = it
                    },
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .fillMaxWidth()
                        .height(63.dp)
                        .background(white),
                    isError = false,
                    placeholder = "비밀번호를 입력해주세요",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isEnable = true,
                    isPassword = true
                )
                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .height(63.dp),
                    onClick = {
                        viewModel.resetPassword()
                    },
                    isEnable = password == passwordCheck,
                    text = "완료"
                )
            }
        }
    }

}