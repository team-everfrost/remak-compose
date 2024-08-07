package com.everfrost.remak_compose.view.home.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.Glide
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak_compose.viewModel.home.add.AddViewModel
import com.everfrost.remak_compose.viewModel.home.add.UploadState
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun AddLoadingScreen(
    navController: NavController,
    viewModel: AddViewModel
) {
    val uploadState by viewModel.uploadState.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))

    // 애니메이션 상태를 설정 (재생 속도, 반복 등)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = uploadState == UploadState.SUCCESS
    )
    val isFileTooLarge by viewModel.isFileTooLarge.collectAsState()

    when {
        isFileTooLarge ->
            CustomConfirmDialog(
                onDismissRequest = {
                    viewModel.setIsFileTooLarge(false)
                },
                mainTitle = "파일이 너무 큽니다",
                subTitle = "파일 크기는 10MB 이하로 제한되어 있습니다",
                btnText = "확인"
            )
    }


    Scaffold(
        containerColor = white,
        modifier = Modifier
            .background(white)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        bottomBar = {
            if (uploadState == UploadState.SUCCESS)
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = {

                        navController.navigate(RemakScreen.Main.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "isUpdate",
                            true
                        )
                    },
                    isEnable = true,
                    text = "확인"
                )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            if (uploadState == UploadState.LOADING) {
                Column(
                    Modifier.align(Alignment.Center)
                ) {
                    GlideImage(
                        imageModel = { R.drawable.loading },
                        modifier = Modifier.size(270.dp)
                    )
                    Text(
                        text = "잠시만 기다려주세요", style = TextStyle(
                            color = black2,
                            fontSize = 20.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else if (uploadState == UploadState.SUCCESS) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )

            }
        } // Box

    }

}