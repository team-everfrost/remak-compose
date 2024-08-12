package com.everfrost.remak_compose.view.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.textBlack3
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.common.button.GrayButton
import com.everfrost.remak_compose.view.home.main.AddDataButton
import com.everfrost.remak_compose.viewModel.home.collection.CollectionViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel
) {

    val collectionList by viewModel.collectionList.collectAsState()
    val collectionListState by viewModel.collectionListState.collectAsState()

    LaunchedEffect(
        true
    ) {
        viewModel.fetchCollectionList()

    }

    Scaffold(
        containerColor = bgGray2,

        bottomBar = {
            BottomNav(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 40.dp)

        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row {
                    Text(
                        text = "컬렉션", style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = pretendard
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (collectionList.isNotEmpty())
                        GrayButton(
                            modifier = Modifier
                                .width(67.dp)
                                .height(30.dp),
                            onClick = { /*TODO*/ },
                            text = "추가하기"
                        )
                }
                if (collectionList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(60.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp
                        ),
                    ) {
                        items(collectionList.size) { index ->
                            CollectionListItem(
                                modifier = Modifier
                                    .height(120.dp),
                                collectionName = collectionList[index].name,
                                collectionDescription = collectionList[index].description,
                                collectionCount = collectionList[index].count,
                            )
                        }

                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
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
                            title = "새 컬렉션 만들기",
                            textStyle = TextStyle(
                                fontFamily = pretendard,
                                fontSize = 16.sp,
                                color = textBlack3,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }// Column

        }

    }
}