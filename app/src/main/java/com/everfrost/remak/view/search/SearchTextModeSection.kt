package com.everfrost.remak.view.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.view.common.layout.FileLayout
import com.everfrost.remak.view.common.layout.ImageLayout
import com.everfrost.remak.view.common.layout.LinkLayout
import com.everfrost.remak.view.common.layout.MemoLayout
import com.everfrost.remak.viewModel.search.SearchViewModel
import kotlinx.coroutines.flow.debounce


@Composable
fun SearchTextModeSection(
    searchList: List<MainListModel.Data>,
    navController: NavController,
    modifier: Modifier,
    viewModel: SearchViewModel
) {

    val scrollState = rememberLazyListState()
    val searchListState by viewModel.searchListState.collectAsState()
    val isDataEnd by viewModel.isDataEnd.collectAsState()



    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= searchList.size - 1 && searchListState !is APIResponse.Loading && !isDataEnd) {
                    viewModel.getTextSearchResult(viewModel.searchContent.value.text)
                }
            }
    }


    LazyColumn(
        state = scrollState,
        modifier = modifier,
    ) {
        items(searchList.size) { index ->
            when (searchList[index].type) {
                "MEMO" -> MemoLayout(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .height(170.dp),
                    date = searchList[index].updatedAt!!,
                    isSelected = searchList[index].isSelected,
                    content = searchList[index].content!!,
                    isEditMode = false,
                    onShortTab = {

                        navController.navigate("MemoDetail/${searchList[index].docId}")

                    },
                    onLongTab = {
                    }

                )

                "FILE" -> FileLayout(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .height(170.dp),
                    title = searchList[index].title!!,
                    date = searchList[index].updatedAt!!,
                    summary = searchList[index].summary,
                    status = searchList[index].status!!,
                    isSelected = searchList[index].isSelected,
                    isEditMode = false,
                    onShortTab = {

                        navController.navigate("FileDetail/${searchList[index].docId}")

                    },
                    onLongTab = {
                    }

                )

                "WEBPAGE" -> LinkLayout(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .height(170.dp),

                    title = searchList[index].title!!,
                    url = searchList[index].url!!,
                    status = searchList[index].status!!,
                    isSelected = searchList[index].isSelected,
                    isEditMode = false,
                    summary = searchList[index].summary,
                    thumbnailUrl = searchList[index].thumbnailUrl,
                    onShortTab = {

                        navController.navigate("LinkDetail/${searchList[index].docId}")
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

                        title = searchList[index].title!!,
                        date = searchList[index].updatedAt!!,
                        thumbnailUrl = searchList[index].thumbnailUrl,
                        summary = searchList[index].summary ?: "",
                        status = searchList[index].status!!,
                        isSelected = searchList[index].isSelected,
                        isEditMode = false,
                        onShortTab = {

                            navController.navigate("ImageDetail/${searchList[index].docId}")
                        },
                        onLongTab = {

                        }

                    )
                }

            }
        }

    }


}