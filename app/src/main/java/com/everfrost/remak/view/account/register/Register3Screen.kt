package com.everfrost.remak.view.account.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.RemakScreen
import com.everfrost.remak.view.account.widget.textfield.AccountTextField
import com.everfrost.remak.view.common.appbar.BackTitleAppBar
import com.everfrost.remak.view.common.button.PrimaryButton
import com.everfrost.remak.viewModel.account.register.RegisterViewModel

@Composable
fun Register3Screen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val password by viewModel.password.collectAsState()
    val isLengthValid by viewModel.isLengthValid.collectAsState()
    val isContainNumber by viewModel.isContainNumber.collectAsState()
    val isContainEnglish by viewModel.isContainEnglish.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()

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
                    text = "비밀번호", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium
                    )
                )
                AccountTextField(
                    value = password,
                    onValueChange = {
                        viewModel.setPassword(it)
                        viewModel.checkPassword(it)
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
                Spacer(modifier = Modifier.padding(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isLengthValid)
                                R.drawable.icon_selected else R.drawable.icon_unselected_check
                        ), contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(
                        text = "9자 이상", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            color = black3,
                            textAlign = TextAlign.Center
                        )
                    )
                    Image(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = painterResource(
                            id = if (isContainNumber)
                                R.drawable.icon_selected else R.drawable.icon_unselected_check
                        ), contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "숫자", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            color = black3,
                            textAlign = TextAlign.Center
                        )
                    )
                    Image(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = painterResource(
                            id = if (isContainEnglish)
                                R.drawable.icon_selected else R.drawable.icon_unselected_check
                        ), contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "영문자", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            color = black3,
                            textAlign = TextAlign.Center
                        )
                    )

                }

                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .height(63.dp),
                    onClick = {
                        navController.navigate(RemakScreen.Register4.route)
                    },
                    isEnable = isPasswordValid,
                    text = "다음"
                )
            }
        }
    }


}