package com.everfrost.remak_compose.view.collection

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.bgRed3
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.red1
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.textBlack3
import com.everfrost.remak_compose.view.common.appbar.DetailAppBar
import com.everfrost.remak_compose.view.common.button.GrayButton
import com.everfrost.remak_compose.view.common.dialog.CustomSelectDialog
import com.everfrost.remak_compose.view.common.layout.FileLayout
import com.everfrost.remak_compose.view.common.layout.ImageLayout
import com.everfrost.remak_compose.view.common.layout.LinkLayout
import com.everfrost.remak_compose.view.common.layout.MemoLayout
import com.everfrost.remak_compose.viewModel.home.collection.CollectionViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.debounce

@Composable
fun CollectionDetailScreen(
    navController: NavController,
    collectionName: String,
    collectionDescription: String?,
    viewModel: CollectionViewModel
) {
    val scrollState = rememberLazyListState()
    val collectionDetailList by viewModel.collectionDetailList.collectAsState()
    val isDataEnd by viewModel.isDataEnd.collectAsState()
    val collectionDetailListState by viewModel.collectionDetailListState.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val selectCount by viewModel.selectCount.collectAsState()
    val haptics = LocalHapticFeedback.current
    val isCollectionDeleteComplete by viewModel.isActionComplete.collectAsState()
    var collectionTitleName by remember {
        mutableStateOf(collectionName)
    }
    val resultState = remember {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("isUpdate")
    }
    var deleteDocumentDialog by remember {
        mutableStateOf(false)
    }
    var deleteCollectionDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(resultState) {
        collectionTitleName = resultState?.value ?: collectionName
    }
    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= collectionDetailList.size - 1 && collectionDetailListState !is APIResponse.Loading && !isDataEnd) {
                    viewModel.fetchCollectionDetailList(collectionName)
                }
            }
    }

    LaunchedEffect(true) {
        viewModel.fetchCollectionDetailList(collectionName)
    }

    LaunchedEffect(isCollectionDeleteComplete) {
        if (isCollectionDeleteComplete == true) {
            navController.popBackStack()
        }

    }

    BackHandler {
        if (isEditMode) {
            viewModel.toggleEditMode()
        } else {
            navController.popBackStack()
        }
    }

    when {
        deleteDocumentDialog ->
            CustomSelectDialog(
                onDismissRequest = { deleteDocumentDialog = false },
                onConfirm = {
                    viewModel.removeDataInCollection(collectionName)
                    deleteDocumentDialog = false
                    viewModel.toggleEditMode()
                    viewModel.resetCollectionDetailList()
                    viewModel.fetchCollectionDetailList(collectionName)

                },
                mainTitle = "${selectCount}개의 자료를 삭제하시겠어요?",
                subTitle = "나중에 다시 추가할 수 있어요",
                confirmBtnText = "삭제하기",
                cancelBtnText = "취소하기"
            )
    }

    when {
        deleteCollectionDialog ->
            CustomSelectDialog(
                onDismissRequest = { deleteCollectionDialog = false },
                onConfirm = {
                    viewModel.deleteCollection(collectionName)
                    deleteCollectionDialog = false

                },
                mainTitle = "컬렉션을 삭제하시겠어요?",
                subTitle = "삭제 시 복구가 불가능해요",
                confirmBtnText = "삭제하기",
                cancelBtnText = "취소하기"
            )

    }
    Scaffold(
        containerColor = bgGray2,
        topBar = {
            DetailAppBar(
                backClick = { navController.popBackStack() },
                title = collectionTitleName,
                isShareEnable = false,
                shareClick = { },
                dropDownMenuContent = { dismissMenu ->
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "컬렉션 수정", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                ),
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = {
                            dismissMenu()
                            navController.navigate("EditCollection/${collectionName}/${collectionDescription}")
                        }
                    )
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "컬렉션 삭제", style = TextStyle(
                                    color = red1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                ),
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = {
                            dismissMenu()
                            deleteCollectionDialog = true
                        }
                    )

                },
                color = bgGray2
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp)

        ) {
            if (collectionDetailListState is APIResponse.Success) {
                if (collectionDetailList.isNotEmpty()) {

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isEditMode) {
                                Text(
                                    text = "${selectCount}개 선택됨", style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = pretendard,
                                        color = black1
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .width(67.dp)
                                        .height(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            color = bgRed3
                                        )
                                        .clickable(
                                            //ripple 효과 제거
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {

                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "삭제하기", style = TextStyle(
                                            color = red1,
                                            fontSize = 14.sp,
                                            fontFamily = pretendard,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.clickable {
                                            deleteDocumentDialog = true
                                        }
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .width(67.dp)
                                        .height(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            color = bgGray2,
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = strokeGray2,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable(
                                            //ripple 효과 제거
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {

                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "이전으로", style = TextStyle(
                                            color = black1,
                                            fontSize = 14.sp,
                                            fontFamily = pretendard,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.clickable(
                                            //ripple 효과 제거
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            viewModel.resetSelect()
                                            viewModel.toggleEditMode()
                                        }
                                    )
                                }


                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                                GrayButton(
                                    modifier = Modifier
                                        .width(67.dp)
                                        .height(30.dp),
                                    onClick = { viewModel.toggleEditMode() },
                                    text = "편집하기"
                                )
                            }

                        }

                        LazyColumn(
                            state = scrollState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(top = 30.dp),
                        ) {
                            items(collectionDetailList.size) { index ->
                                when (collectionDetailList[index].type) {
                                    "MEMO" -> MemoLayout(
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                            .fillMaxWidth()
                                            .height(170.dp),
                                        date = collectionDetailList[index].updatedAt!!,
                                        isSelected = collectionDetailList[index].isSelected,
                                        content = collectionDetailList[index].content!!,
                                        isEditMode = isEditMode,
                                        onShortTab = {
                                            if (isEditMode) {
                                                viewModel.toggleSelect(index)
                                            } else {
                                                navController.navigate("MemoDetail/${collectionDetailList[index].docId}")
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
                                        title = collectionDetailList[index].title!!,
                                        date = collectionDetailList[index].updatedAt!!,
                                        summary = collectionDetailList[index].summary,
                                        status = collectionDetailList[index].status!!,
                                        isSelected = collectionDetailList[index].isSelected,
                                        isEditMode = isEditMode,
                                        onShortTab = {
                                            if (isEditMode) {
                                                viewModel.toggleSelect(index)
                                            } else {

                                                navController.navigate("FileDetail/${collectionDetailList[index].docId}")
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

                                        title = collectionDetailList[index].title!!,
                                        url = collectionDetailList[index].url!!,
                                        status = collectionDetailList[index].status!!,
                                        isSelected = collectionDetailList[index].isSelected,
                                        isEditMode = isEditMode,
                                        summary = collectionDetailList[index].summary,
                                        thumbnailUrl = collectionDetailList[index].thumbnailUrl,
                                        onShortTab = {
                                            if (isEditMode) {
                                                viewModel.toggleSelect(index)
                                            } else {
                                                navController.navigate("LinkDetail/${collectionDetailList[index].docId}")
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

                                            title = collectionDetailList[index].title!!,
                                            date = collectionDetailList[index].updatedAt!!,
                                            thumbnailUrl = collectionDetailList[index].thumbnailUrl,
                                            summary = collectionDetailList[index].summary ?: "",
                                            status = collectionDetailList[index].status!!,
                                            isSelected = collectionDetailList[index].isSelected,
                                            isEditMode = isEditMode,
                                            onShortTab = {
                                                if (isEditMode) {
                                                    viewModel.toggleSelect(index)
                                                } else {

                                                    navController.navigate("ImageDetail/${collectionDetailList[index].docId}")
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

                                }
                            }

                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        GlideImage(
                            imageModel = { R.drawable.icon_empty_box },
                            modifier = Modifier
                                .width(108.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                        Text(
                            text = "컬렉션 정보가 없어요", style = TextStyle(
                                fontFamily = pretendard,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = textBlack3
                            ),
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }

            }
        }
    }


}