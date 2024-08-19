package com.everfrost.remak.view.home.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.everfrost.remak.R
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.ui.theme.bgGray2
import com.everfrost.remak.ui.theme.black2
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.red1
import com.everfrost.remak.ui.theme.textBlack3
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.BottomNav
import com.everfrost.remak.view.RemakScreen
import com.everfrost.remak.view.collection.CollectionBottomSheet
import com.everfrost.remak.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak.view.common.dialog.CustomSelectDialog
import com.everfrost.remak.view.common.layout.FileLayout
import com.everfrost.remak.view.common.layout.ImageLayout
import com.everfrost.remak.view.common.layout.LinkLayout
import com.everfrost.remak.view.common.layout.MemoLayout
import com.everfrost.remak.viewModel.collection.CollectionViewModel
import com.everfrost.remak.viewModel.home.main.HomeMainViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeMainScreen(
    navController: NavController,
    viewModel: HomeMainViewModel,
    collectionViewModel: CollectionViewModel
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
    val deleteDialog by viewModel.deleteDialog.collectAsState()
    val isTokenExpired by viewModel.isTokenExpired.collectAsState()


    var collectionBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val scope = rememberCoroutineScope()

    when {
        isTokenExpired ->
            CustomConfirmDialog(
                onDismissRequest = {
                    viewModel.setTokenExpired(false)
                    navController.navigate(RemakScreen.OnBoarding.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                mainTitle = "로그인 정보가 만료되었습니다",
                subTitle = "다시 로그인해주세요",
                btnText = "확인"
            )

    }

    when {
        deleteDialog ->
            CustomSelectDialog(
                onDismissRequest = {
                    viewModel.setDeleteDialog(false)
                },
                onConfirm = {
                    viewModel.setDeleteDialog(false)
                    viewModel.deleteDocument()
                    viewModel.toggleEditMode()
                },
                mainTitle = "삭제하시겠습니까?",
                subTitle = "삭제시 복구가 불가능해요",
                confirmBtnText = "삭제하기",
                cancelBtnText = "취소하기"
            )
    }

    if (!isInit) {
        LaunchedEffect(Unit) {
            viewModel.fetchMainList()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("isUpdate")) {
        val resultState =
            navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("isUpdate")?.value
        Log.d("HomeMainScreen", "resultState: $resultState")
        if (resultState == true) {
            viewModel.resetMainList()
            viewModel.fetchMainList()
            navController.currentBackStackEntry?.savedStateHandle?.set(
                "isUpdate",
                null
            ) // 이후 상태 초기화
        }
    }

    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= mainList.size - 1 && mainListState !is APIResponse.Loading && !isDataEnd && !isEditMode) {
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
        } else {
            navController.popBackStack()

        }
    }
    Scaffold(
        containerColor = white,
        bottomBar = {
            if (!isEditMode) {
                BottomNav(navController = navController)
            } else {
                BottomAppBar(
                    containerColor = white,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                    ) {
                        Text(
                            text = "+컬렉션", style = TextStyle(
                                fontFamily = pretendard,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = black2
                            ),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                collectionBottomSheet = true

                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "삭제하기", style = TextStyle(
                                fontFamily = pretendard,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = red1
                            ),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                viewModel.setDeleteDialog(true)
                            }
                        )

                    }

                }
            }
        },
    ) { innerPadding ->
        if (collectionBottomSheet) {
            CollectionBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        collectionBottomSheet = false
                    }
                }, sheetState =
                sheetState,
                collectionViewModel,
                viewModel.getSelectedDocument(),
                onConfirm = {
                    viewModel.toggleEditMode()
                }
            )
        }

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
                        onClick = {
                            navController.navigate(RemakScreen.Add.route)
                        },
                        title = "추가하러 가기",
                        textStyle = TextStyle(
                            fontFamily = pretendard,
                            fontSize = 16.sp,
                            color = textBlack3,
                            fontWeight = FontWeight.Medium
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
                                onShortTab = {
                                    if (isEditMode) {
                                        viewModel.toggleSelect(index)
                                    } else {
                                        navController.navigate("MemoDetail/${mainList[index].docId}")
                                    }
                                },
                                onLongTab = {
                                    if (!isEditMode) {
                                        haptics.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        viewModel.toggleEditMode()
                                        viewModel.toggleSelect(index)
                                    }
                                }

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
                                    if (isEditMode) {
                                        viewModel.toggleSelect(index)
                                    } else {
                                        navController.navigate("FileDetail/${mainList[index].docId}")
                                    }
                                },
                                onLongTab = {
                                    if (!isEditMode) {
                                        haptics.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        viewModel.toggleEditMode()
                                        viewModel.toggleSelect(index)
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
                                thumbnailUrl = mainList[index].thumbnailUrl,
                                onShortTab = {
                                    if (isEditMode) {
                                        viewModel.toggleSelect(index)
                                    } else {
                                        navController.navigate("LinkDetail/${mainList[index].docId}")
                                    }
                                },
                                onLongTab = {
                                    if (!isEditMode) {
                                        haptics.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        viewModel.toggleEditMode()
                                        viewModel.toggleSelect(index)
                                    }
                                }
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
                                    summary = mainList[index].summary ?: "",
                                    status = mainList[index].status!!,
                                    isSelected = mainList[index].isSelected,
                                    isEditMode = isEditMode,
                                    onShortTab = {
                                        if (isEditMode) {
                                            viewModel.toggleSelect(index)
                                        } else {
                                            navController.navigate("ImageDetail/${mainList[index].docId}")
                                        }
                                    },
                                    onLongTab = {
                                        if (!isEditMode) {
                                            haptics.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )
                                            viewModel.toggleEditMode()
                                            viewModel.toggleSelect(index)
                                        }
                                    }

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
            MainAppBar(scrollUpState = scrollUpState,
                isEditMode = isEditMode,
                onAddClick = {
                    navController.navigate(RemakScreen.Add.route)
                },
                onMoreClick = {
                    viewModel.toggleEditMode()
                },
                backButtonClick = {
                    viewModel.toggleEditMode()
                }

            )
            PullRefreshIndicator(
                refreshing = mainListState is APIResponse.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }
    }
}