package com.everfrost.remak_compose.view.tag

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.layout.FileLayout
import com.everfrost.remak_compose.view.common.layout.ImageLayout
import com.everfrost.remak_compose.view.common.layout.LinkLayout
import com.everfrost.remak_compose.view.common.layout.MemoLayout
import com.everfrost.remak_compose.viewModel.home.tag.TagViewModel
import kotlinx.coroutines.flow.debounce


@Composable
fun TagDetailScreen(
    tagName: String,
    navController: NavController,
    viewModel: TagViewModel,
) {
    val tagDetailList by viewModel.tagDetailList.collectAsState()
    val scrollState = rememberLazyListState()
    val tagDetailListState by viewModel.tagDetailListState.collectAsState()
    val isDataEnd by viewModel.isDataEnd.collectAsState()
    val tagCount by viewModel.tagCount.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchTagDetailList(tagName)
    }

    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= tagDetailList.size - 1 && tagDetailListState !is APIResponse.Loading && !isDataEnd) {
                    viewModel.fetchTagList()
                }
            }
    }
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            BackTitleAppBar(navController = navController, title = "$tagName (${tagCount})")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                items(tagDetailList.size) { index ->
                    when (tagDetailList[index].type) {
                        "MEMO" -> MemoLayout(
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .height(170.dp),
                            date = tagDetailList[index].updatedAt!!,
                            isSelected = tagDetailList[index].isSelected,
                            content = tagDetailList[index].content!!,
                            isEditMode = false,
                            onShortTab = {

                                navController.navigate("MemoDetail/${tagDetailList[index].docId}")

                            },
                            onLongTab = {
                            }

                        )

                        "FILE" -> FileLayout(
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .height(170.dp),
                            title = tagDetailList[index].title!!,
                            date = tagDetailList[index].updatedAt!!,
                            summary = tagDetailList[index].summary,
                            status = tagDetailList[index].status!!,
                            isSelected = tagDetailList[index].isSelected,
                            isEditMode = false,
                            onShortTab = {

                                navController.navigate("FileDetail/${tagDetailList[index].docId}")

                            },
                            onLongTab = {
                            }

                        )

                        "WEBPAGE" -> LinkLayout(
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .height(170.dp),

                            title = tagDetailList[index].title!!,
                            url = tagDetailList[index].url!!,
                            status = tagDetailList[index].status!!,
                            isSelected = tagDetailList[index].isSelected,
                            isEditMode = false,
                            summary = tagDetailList[index].summary,
                            thumbnailUrl = tagDetailList[index].thumbnailUrl,
                            onShortTab = {

                                navController.navigate("LinkDetail/${tagDetailList[index].docId}")
                            },
                            onLongTab = {

                            }
                        )

                        "IMAGE" -> {
                            ImageLayout(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                                    .height(170.dp),

                                title = tagDetailList[index].title!!,
                                date = tagDetailList[index].updatedAt!!,
                                thumbnailUrl = tagDetailList[index].thumbnailUrl,
                                summary = tagDetailList[index].summary ?: "",
                                status = tagDetailList[index].status!!,
                                isSelected = tagDetailList[index].isSelected,
                                isEditMode = false,
                                onShortTab = {

                                    navController.navigate("ImageDetail/${tagDetailList[index].docId}")
                                },
                                onLongTab = {

                                }

                            )
                        }

                    }
                }

            }
        }
    }
}