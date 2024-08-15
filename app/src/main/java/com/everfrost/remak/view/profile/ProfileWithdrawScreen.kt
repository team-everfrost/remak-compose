package com.everfrost.remak.view.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.primaryBlue
import com.everfrost.remak.ui.theme.red1
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.common.appbar.BackTitleAppBar
import com.everfrost.remak.view.common.dialog.CustomSelectDialog
import com.everfrost.remak.viewModel.profile.ProfileViewModel


@Composable
fun ProfileWithdrawScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    )
    val codeValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(6) {
        remember {
            FocusRequester()
        }
    }

    val email by viewModel.userEmail.collectAsState()
    val isVerifySuccess by viewModel.isVerifySuccess.collectAsState()

    LaunchedEffect(true) {
        viewModel.getWithdrawVerifyCode()
        focusRequesters[0].requestFocus()
    }

    when {
        isVerifySuccess == true ->
            CustomSelectDialog(
                onDismissRequest = {
                    viewModel.setIsVerifySuccess(null)
                },
                onConfirm = {
                    viewModel.withdraw()
                    viewModel.setIsVerifySuccess(null)
                },
                mainTitle = "정말로 탈퇴하시겠어요?",
                subTitle = "회원탈퇴를 하시면 데이터가 모두 삭제돼요\n또한, 모든 데이터는 복구가 불가능해요",
                confirmBtnText = "네",
                cancelBtnText = "아니오"
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
                if (isVerifySuccess == null || isVerifySuccess == true) {
                    Row {
                        Text(
                            text = email,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = pretendard,
                                color = primaryBlue
                            )
                        )
                        Text(
                            text = "로 발송된",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = pretendard,
                                color = black1
                            )
                        )
                    }
                    Text(
                        text = "인증번호를 입력해주세요",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black1
                        )
                    )
                } else {
                    Text(
                        text = "인증번호가 일치하지 않습니다\n다시 확인해주세요",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = red1
                        )
                    )
                }


                Row(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    codeValues.forEachIndexed { index, value ->
                        CodeTextField(
                            customTextSelectionColors = customTextSelectionColors,
                            codeValue = codeValues[index],
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    codeValues[index] = newValue
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                                if (codeValues.all { it.isNotEmpty() }) {
                                    val verifyCode = codeValues.joinToString("")
                                    viewModel.verifyWithdrawCode(verifyCode)
                                }
                            },
                            onBackspace = {
                                Log.d("Register2Screen", "onBackspace: $index")
                                codeValues[index] = ""
                                if (index > 0 && codeValues[index].isEmpty()) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            modifier = Modifier
                                .focusRequester(focusRequesters[index]),
                            isError = isVerifySuccess == false
                        )
                    }
                }


            }
        }
    }


}
