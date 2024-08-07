package com.everfrost.remak_compose.view.home.File

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.appbar.DetailAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.layout.TagBox
import com.everfrost.remak_compose.view.common.layout.TagRowLayout
import com.everfrost.remak_compose.viewModel.home.file.FileDetailViewModel

@Composable
fun FileDetailScreen(
    navController: NavController,
    viewModel: FileDetailViewModel,
    docIdx: String? = "c35a8041-43f3-47ab-9581-1d1ad38751f2"

) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            DetailAppBar(
                backClick = { /*TODO*/ },
                title = "파일",
                isShareEnable = true,
                shareClick = { /*TODO*/ },
                dropDownMenuContent = {
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "편집하기", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                        }
                    )
                }
            )
        },
        bottomBar = {
            PrimaryButton(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 16.dp),
                onClick = { /*TODO*/ },
                isEnable = true,
                text = "다운로드",
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
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(bgGray2, shape = RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = strokeGray2,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_file_gray),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(96.dp)
                    )
                }
                Text(
                    text = "2023.11.22", style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = pretendard,
                        color = black3,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    text = "2023.11.22", style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = pretendard,
                        color = black1,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                )

                TagRowLayout(
                    modifier = Modifier.padding(top = 24.dp),
                    tags = listOf(
                        "태그1",
                        "태그2",
                        "태그3",
                        "태그4",
                        "태그5",
                        "태그6",
                        "태그7",
                        "태그8",
                        "태그9",
                        "태그10"
                    ),
                    onClick = { /*TODO*/ }
                )

                Text(
                    text = "요약", style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = pretendard,
                        color = black1,
                        fontWeight = FontWeight.Bold
                    ), modifier = Modifier.padding(top = 24.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .background(white, shape = RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = strokeGray2,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "아주길고긴 요약을 써줘 아주길고긴 요약을 써줘 아주길" +
                                "고긴 요약을 써줘 아주길고긴 요약을 써줘 " +
                                "아주길고긴 요약을 써줘" +
                                " 아주길고긴 요약을 써줘 아주길고긴 요약을 써줘 아주길고긴 요약을 써줘 ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            color = black3,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                    )
                }
                Box(modifier = Modifier.height(16.dp))

            } // Column
        }

    }


}