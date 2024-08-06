package com.everfrost.remak_compose.view.home.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.textBlack3
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.common.layout.FileLayout
import com.everfrost.remak_compose.view.common.layout.ImageLayout
import com.everfrost.remak_compose.view.common.layout.LinkLayout
import com.everfrost.remak_compose.view.common.layout.MemoLayout
import com.everfrost.remak_compose.view.tool.customHeightBasedOnWidth
import com.everfrost.remak_compose.viewModel.home.main.HomeMainViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeMainScreen(
    navController: NavController,
    viewModel: HomeMainViewModel
) {
    val scrollState = rememberLazyListState()
    val scrollUpState by viewModel.scrollUp.collectAsState()
    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    val mainListState by viewModel.mainListState.collectAsState()
    val mainList by viewModel.mainList.collectAsState()
    val isDataEnd by viewModel.isDataEnd.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val haptics = LocalHapticFeedback.current
    val isInit by viewModel.isInit.collectAsState()

    if (!isInit) {
        LaunchedEffect(Unit) {
            viewModel.fetchMainList()
        }
    }

    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= mainList.size - 1 && mainListState !is APIResponse.Loading && !isDataEnd) {
                    viewModel.fetchMainList()
                }
            }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = mainListState is APIResponse.Loading,
        onRefresh = {
            viewModel.resetMainList()
            viewModel.fetchMainList()
        })

    BackHandler {
        if (isEditMode) {
            viewModel.toggleEditMode()
        }
    }
    Scaffold(
        containerColor = white,
        bottomBar = {
            if (!isEditMode) {
                BottomNav(navController = navController)
            }
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
                .background(bgGray2),
        ) {

            if (mainListState is APIResponse.Success && mainList.isEmpty()) {

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    GlideImage(
                        imageModel = { R.drawable.icon_empty_box },
                        modifier = Modifier
                            .width(108.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                    Text(
                        text = "등록된 정보가 없어요", style = TextStyle(
                            fontFamily = pretendard,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = textBlack3
                        ),
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    AddDataButton(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .width(148.dp)
                            .height(40.dp)
                            .align(Alignment.CenterHorizontally),
                        onClick = { /*TODO*/ },
                        title = "추가하러 가기",
                        textStyle = TextStyle(
                            fontFamily = pretendard,
                            fontSize = 16.sp,
                            color = textBlack3,
                        )
                    )
                }
            } else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 50.dp)
                ) {
                    items(mainList.size) { index ->
                        when (mainList[index].type) {
                            "MEMO" -> MemoLayout(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                                    .height(170.dp),

                                date = mainList[index].updatedAt!!,
                                isSelected = mainList[index].isSelected,
                                content = mainList[index].content!!,
                                isEditMode = isEditMode,

                                )

                            "FILE" -> FileLayout(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                                    .height(170.dp),
                                title = mainList[index].title!!,
                                date = mainList[index].updatedAt!!,
                                summary = mainList[index].summary,
                                status = mainList[index].status!!,
                                isSelected = mainList[index].isSelected,
                                isEditMode = isEditMode,
                                onShortTab = {
                                    Log.d("HomeMainScreen", "onShortTab")
                                },
                                onLongTab = {
                                    if (isEditMode) {

                                    } else {
                                        haptics.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        viewModel.toggleEditMode()
                                    }
                                }

                            )

                            "WEBPAGE" -> LinkLayout(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                                    .height(170.dp),

                                title = mainList[index].title!!,
                                url = mainList[index].url!!,
                                status = mainList[index].status!!,
                                isSelected = mainList[index].isSelected,
                                isEditMode = isEditMode,

                                summary = mainList[index].summary,
                                thumbnailUrl = mainList[index].thumbnailUrl
                            )

                            "IMAGE" -> {
                                ImageLayout(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .fillMaxWidth()
                                        .height(170.dp),

                                    title = mainList[index].title!!,
                                    date = mainList[index].updatedAt!!,
                                    thumbnailUrl = mainList[index].thumbnailUrl,
                                    summary = mainList[index].summary!!,
                                    status = mainList[index].status!!,
                                    isSelected = mainList[index].isSelected,
                                    isEditMode = isEditMode,

                                    )
                            }

                            "DATE" -> Text(
                                modifier = Modifier.padding(top = 50.dp, bottom = 10.dp),
                                text = mainList[index].header!!, style = TextStyle(
                                    fontFamily = pretendard,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold

                                )
                            )

                        }

                    }

                }
            }
            MainAppBar(scrollUpState = scrollUpState)
            PullRefreshIndicator(
                refreshing = mainListState is APIResponse.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }
    }
}