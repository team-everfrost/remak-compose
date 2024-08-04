package com.everfrost.remak_compose.view.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.gray2
import com.everfrost.remak_compose.ui.theme.gray3
import com.everfrost.remak_compose.ui.theme.white
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
            // Box와 contentPadding을 통해 LazyColumn의 위치를 잡아준다.
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gray2),
                contentPadding = PaddingValues(top = 56.dp)
            ) {
                // 실제 구현에는 성능 개선을 위해 key 값을 설정해야 한다.
                items(100) {
                    Text(
                        text = "Hello Compose! $it",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }

            MainAppBar(scrollUpState = scrollUpState)

        }
    }
}