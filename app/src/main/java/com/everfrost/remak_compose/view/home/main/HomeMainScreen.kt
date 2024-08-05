package com.everfrost.remak_compose.view.home.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.layout.FileLayout
import com.everfrost.remak_compose.view.common.layout.ImageLayout
import com.everfrost.remak_compose.view.common.layout.LinkLayout
import com.everfrost.remak_compose.view.common.layout.MemoLayout
import com.everfrost.remak_compose.view.tool.customHeightBasedOnWidth
import com.everfrost.remak_compose.viewModel.home.main.HomeMainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMainScreen() {
    val viewModel: HomeMainViewModel = hiltViewModel()
    val scrollState = rememberLazyListState()
    val scrollUpState by viewModel.scrollUp.collectAsState()
    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    val mainListState by viewModel.mainListState.collectAsState()
    val mainList by viewModel.mainList.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchMainList()

    }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1500)
            isRefreshing = false
        }
    }





    Scaffold(
        containerColor = white
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding),
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = state
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(bgGray2)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 50.dp)
            ) {
                items(mainList.size) { index ->
                    when (mainList[index].type) {
                        "MEMO" -> MemoLayout(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth()
                                .customHeightBasedOnWidth(0.4f),
                            date = mainList[index].updatedAt!!,
                            isSelected = mainList[index].isSelected,
                            content = mainList[index].content!!,
                            isEditMode = false
                        )

                        "FILE" -> FileLayout(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth()
                                .customHeightBasedOnWidth(0.4f),
                            title = mainList[index].title!!,
                            date = mainList[index].updatedAt!!,
                            summary = mainList[index].summary,
                            status = mainList[index].status!!,
                            isSelected = mainList[index].isSelected

                        )

                        "WEBPAGE" -> LinkLayout(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth()
                                .customHeightBasedOnWidth(0.4f),
                            title = mainList[index].title!!,
                            url = mainList[index].url!!,
                            status = mainList[index].status!!,
                            isSelected = mainList[index].isSelected,
                            isEditMode = false,
                            summary = mainList[index].summary,
                            thumbnailUrl = mainList[index].thumbnailUrl
                        )

                        "IMAGE" -> {
                            Log.d("HomeMainScreen", mainList[index].toString())
                            ImageLayout(
                                modifier = Modifier
                                    .padding(bottom = 5.dp)
                                    .fillMaxWidth()
                                    .customHeightBasedOnWidth(0.4f),
                                title = mainList[index].title!!,
                                date = mainList[index].updatedAt!!,
                                thumbnailUrl = mainList[index].thumbnailUrl,
                                summary = mainList[index].summary!!,
                                status = mainList[index].status!!,
                                isSelected = mainList[index].isSelected
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
            MainAppBar(scrollUpState = scrollUpState)

        }
    }
}