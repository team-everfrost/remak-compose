package com.everfrost.remak_compose.view.account.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black2
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.viewModel.account.register.RegisterViewModel

@Composable
fun RegisterAgreeScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {

    val privacyAgree by viewModel.privacyAgree.collectAsState()
    val serviceAgree by viewModel.serviceAgree.collectAsState()
    val allAgree by viewModel.allAgree.collectAsState()
    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = ""
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
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = "서비스 이용약관 동의", style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "서비스 시작 및 가입을 위해 먼저\n" +
                            "가입 및 정보 제공에 동의해 주세요", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        color = black3
                    )
                )
                Spacer(modifier = Modifier.padding(30.dp))

                Row(
                    modifier = Modifier.clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) { viewModel.toggleAllAgree() },
                ) {
                    Image(
                        painter = painterResource(
                            id =
                            if (allAgree) R.drawable.icon_selected else R.drawable.icon_unselected
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text(
                        text = "이용약관 동의 (전체)", style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Bold,
                            color = black2
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(strokeGray2)
                )

                Row(
                    Modifier
                        .padding(top = 17.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) { viewModel.toggleServiceAgree() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (serviceAgree) R.drawable.icon_selected else R.drawable.icon_unselected),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text(
                        text = "서비스 이용약관 (필수)", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Normal,
                            color = black3,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null
                    )
                }

                Row(
                    Modifier
                        .padding(top = 17.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        )
                        { viewModel.togglePrivacyAgree() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (privacyAgree) R.drawable.icon_selected else R.drawable.icon_unselected),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    Text(
                        text = "개인정보 수집 및 이용동의 (필수)", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Normal,
                            color = black3,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    modifier =
                    Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth()
                        .height(
                            63.dp
                        ), onClick = {
                        navController.navigate(RemakScreen.Register1.route)
                    }, isEnable = allAgree, text = "다음"
                )

            } // Column

        }

    }

}