package com.everfrost.remak_compose.view.home.main

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.layout.ImageLayout
import com.everfrost.remak_compose.view.tool.customHeightBasedOnWidth
import com.everfrost.remak_compose.viewModel.home.main.HomeMainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMainScreen() {
    val viewModel: HomeMainViewModel = hiltViewModel()
    val scrollState = rememberLazyListState()
    val scrollUpState by viewModel.scrollUp.collectAsState()
    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    Scaffold(
        containerColor = white
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(bgGray2)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 106.dp)
            ) {
                // 실제 구현에는 성능 개선을 위해 key값을 설정해야 한다.
                items(1) {
                    ImageLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .customHeightBasedOnWidth(0.44f)

                    )
                }
            }

            MainAppBar(scrollUpState = scrollUpState)

        }
    }
}