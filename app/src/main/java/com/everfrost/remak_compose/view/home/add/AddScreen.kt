package com.everfrost.remak_compose.view.home.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.viewModel.home.add.AddTopBar
import com.everfrost.remak_compose.viewModel.home.add.AddViewModel
import com.everfrost.remak_compose.viewModel.home.add.UploadState

@Composable
fun AddScreen(
    navController: NavController,
    viewModel: AddViewModel
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.processSelectedUris(context, uris)
            }
        }

    val uploadState by viewModel.uploadState.collectAsState()

    LaunchedEffect(uploadState) {
        if (uploadState == UploadState.LOADING) {
            navController.navigate(RemakScreen.AddLoading.route)
        }
    }
    Scaffold(
        containerColor = white,
        topBar = {
            AddTopBar(
                navController = navController,
                modifier = Modifier
                    .background(white)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 26.dp)
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    text = "추가하기", style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendard,
                        color = black1
                    )
                )

                AddRowButton(
                    onClick = { navController.navigate(RemakScreen.AddLoading.route) },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    icon = R.drawable.icon_link,
                    title = "링크",
                    description = "다시 보고싶은 페이지를 저장할 수 있어요"
                )

                AddRowButton(
                    onClick = { launcher.launch("*/*") },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    icon = R.drawable.icon_file,
                    title = "파일",
                    description = "pdf, txt의 요약을 지원해요"
                )

                AddRowButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    icon = R.drawable.icon_picture,
                    title = "사진",
                    description = "jpg, png, gif등을 지원해요"
                )

                AddRowButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    icon = R.drawable.icon_memo,
                    title = "메모",
                    description = "간단한 메모를 입력할 수 있어요"
                )


            }
        }

    }


}