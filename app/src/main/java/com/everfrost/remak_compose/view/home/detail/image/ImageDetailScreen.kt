package com.everfrost.remak_compose.view.home.detail.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.common.appbar.DetailAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.layout.TagRowLayout
import com.everfrost.remak_compose.view.home.detail.SummaryBoxWidget
import com.everfrost.remak_compose.viewModel.home.detail.image.ImageDetailViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageDetailScreen(
    navController: NavController,
    viewModel: ImageDetailViewModel,
    docIdx: String? = "e73e3b1f-32ef-4a28-8f22-863018292e98"

) {
    val scrollState = rememberScrollState()
    val getDetailDataState by viewModel.getDetailDataState.collectAsState()
    val date by viewModel.date.collectAsState()
    val title by viewModel.title.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val tagList by viewModel.tagList.collectAsState()
    val thumbnailUrl by viewModel.thumbnailUrl.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchDetailData(docIdx!!)
    }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            DetailAppBar(
                backClick = { /*TODO*/ },
                title = "이미지",
                isShareEnable = true,
                shareClick = { viewModel.shareFile(context) },
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
            if (getDetailDataState is APIResponse.Success)
                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    onClick = { viewModel.downloadFile(context) },
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
            if (getDetailDataState is APIResponse.Success) {
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
                            .clickable(
                                //ripple 제거
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigate(RemakScreen.ImageViewer.route)
                            }
                    ) {
                        GlideImage(
                            imageModel = {
                                thumbnailUrl
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))

                        )
                    }
                    Text(
                        text = date, style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = pretendard,
                            color = black3,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(top = 24.dp)
                    )
                    Text(
                        text = title, style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = pretendard,
                            color = black1,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    TagRowLayout(
                        modifier = Modifier.padding(top = 24.dp),
                        tags = tagList,
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

                    SummaryBoxWidget(summary = summary)
                    Box(modifier = Modifier.height(16.dp))

                } // Column
            }
        }

    }


}