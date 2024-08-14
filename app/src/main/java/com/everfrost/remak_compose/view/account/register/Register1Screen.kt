package com.everfrost.remak_compose.view.account.register

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.account.widget.textfield.AccountTextField
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak_compose.viewModel.account.register.RegisterViewModel

@Composable
fun Register1Screen(
    navController: NavController,
    viewModel: RegisterViewModel
) {

    val email by viewModel.email.collectAsState()
    val isValidEmail by viewModel.isValidEmail.collectAsState()
    val isDuplicateEmail by viewModel.isDuplicateEmail.collectAsState()
    val isEmailSendSuccess by viewModel.isEmailSendSuccess.collectAsState()

    LaunchedEffect(isEmailSendSuccess) {
        if (isEmailSendSuccess) {
            navController.navigate(RemakScreen.Register2.route)
            viewModel.setIsEmailSendSuccess(false)
        }
    }

    when {
        isDuplicateEmail ->
            CustomConfirmDialog(
                onDismissRequest = { viewModel.setIsDuplicateEmail(false) },
                mainTitle = "중복된 이메일입니다",
                subTitle = "",
                btnText = "확인"
            )
    }
    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = "회원가입"
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
                    text = "이메일", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium
                    )
                )
                AccountTextField(
                    value = email,
                    onValueChange = {
                        viewModel.setEmail(it)
                        viewModel.checkIsValidEmail()
                    },
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .fillMaxWidth()
                        .height(63.dp)
                        .background(white),
                    isError = !isValidEmail,
                    placeholder = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isEnable = true
                )

                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .height(63.dp),
                    onClick = {
                        viewModel.getVerifyCode()
                    },
                    isEnable = isValidEmail,
                    text = "다음"
                )
            }

        }
    }


}