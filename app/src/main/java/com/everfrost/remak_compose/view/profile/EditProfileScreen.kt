package com.everfrost.remak_compose.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black2
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.dialog.CustomSelectDialog
import com.everfrost.remak_compose.viewModel.account.resetPassword.ResetPasswordViewModel
import com.everfrost.remak_compose.viewModel.profile.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    resetPasswordViewModel: ResetPasswordViewModel
) {
    val email by viewModel.userEmail.collectAsState()

    LaunchedEffect(email) {
        resetPasswordViewModel.setEmail(email)
    }

    var logoutState by remember {
        mutableStateOf(false)
    }

    when {
        logoutState ->
            CustomSelectDialog(
                onDismissRequest = {
                    logoutState = false
                },
                onConfirm = {
                    logoutState = false
                    viewModel.logout()
                    navController.navigate(RemakScreen.OnBoarding.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                mainTitle = "로그아웃",
                subTitle = "정말 로그아웃 하시겠습니까?",
                confirmBtnText = "네",
                cancelBtnText = "아니오"
            )

    }

    Scaffold(
        topBar = {
            BackTitleAppBar(navController = navController, title = "프로필", color = bgGray2)
        },
        containerColor = bgGray2,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "이메일",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = pretendard,
                                color = black3
                            )
                        )
                        Text(
                            text = email,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = pretendard,
                                color = black2
                            )
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = black1,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(strokeGray2)
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            resetPasswordViewModel.setEmail(email)
                            navController.navigate(RemakScreen.ProfileResetPassword1.route)

                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "비밀번호",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = pretendard,
                                color = black3
                            )
                        )
                        Text(
                            text = "*********",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = pretendard,
                                color = black2
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = black1,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(strokeGray2)
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            logoutState = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "로그아웃",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black2
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = black1,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(strokeGray2)
                        .fillMaxWidth()

                )
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate(RemakScreen.ProfileWithdraw.route)
                        },
                    text = "화원탈퇴",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard,
                        color = black3,
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    ),
                )

            }
        }

    }


}