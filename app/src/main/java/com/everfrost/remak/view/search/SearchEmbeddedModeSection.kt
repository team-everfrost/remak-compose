package com.everfrost.remak.view.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.ui.theme.black4
import com.everfrost.remak.ui.theme.gray3
import com.everfrost.remak.view.common.layout.FileLayout
import com.everfrost.remak.view.common.layout.ImageLayout
import com.everfrost.remak.view.common.layout.LinkLayout
import com.everfrost.remak.view.common.layout.MemoLayout
import com.everfrost.remak.viewModel.search.SearchViewModel
import com.valentinilk.shimmer.shimmer


@Composable
fun SearchEmbeddedModeSection(
    navController: NavController,
    viewModel: SearchViewModel,
    modifier: Modifier
) {
    val searchListState by viewModel.searchListState.collectAsState()
    val searchList by viewModel.searchList.collectAsState()

    if (searchListState is APIResponse.Loading) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(170.dp)
                    .background(gray3, shape = RoundedCornerShape(12.dp))
                    .shimmer()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(black4, shape = RoundedCornerShape(12.dp))
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(170.dp)
                    .background(gray3, shape = RoundedCornerShape(12.dp))
                    .shimmer()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(black4, shape = RoundedCornerShape(12.dp))
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(170.dp)
                    .background(gray3, shape = RoundedCornerShape(12.dp))
                    .shimmer()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(black4, shape = RoundedCornerShape(12.dp))
                )
            }
        }
    } else {
        LazyColumn(
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

}