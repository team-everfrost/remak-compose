package com.everfrost.remak.view.home.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.RemakScreen
import com.everfrost.remak.view.common.button.PrimaryButton
import com.everfrost.remak.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak.view.common.textField.RoundGrayTextField
import com.everfrost.remak.viewModel.home.add.AddTopBar
import com.everfrost.remak.viewModel.home.add.AddViewModel

@Composable
fun LinkAddScreen(
    navController: NavController,
    viewModel: AddViewModel
) {
    val linkText by viewModel.linkText.collectAsState()
    val isActionComplete by viewModel.isActionComplete.collectAsState()

    when {
        isActionComplete ->
            CustomConfirmDialog(
                onDismissRequest = {
                    viewModel.setIsActionComplete(false)
                    navController.navigate(RemakScreen.Main.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                mainTitle = "링크 추가",
                subTitle = "링크가 추가되었습니다.",
                btnText = "확인",
            )
    }
    Scaffold(
        containerColor = white,
        topBar = {
            AddTopBar(navController = navController, modifier = Modifier)
        },
        bottomBar = {
            PrimaryButton(
                modifier = Modifier
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = { viewModel.createWebPage() },
                isEnable = linkText.isNotEmpty(),
                text = "추가하기"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "링크 추가하기", style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        color = black1
                    )
                )
                Text(
                    text = "엔터 또는 공백으로 구분하여 여러 링크를 추가하세요", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        color = black3
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )

                RoundGrayTextField(
                    value = linkText,
                    onValueChange = { viewModel.setLinkText(it) },
                    placeholder = "예시: www.naver.com www.google.com...",
                    keyboardOptions = KeyboardOptions(),
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(240.dp)
                )


            }

        }
    }

}