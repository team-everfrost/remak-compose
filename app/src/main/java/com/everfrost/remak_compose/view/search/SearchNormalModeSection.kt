package com.everfrost.remak_compose.view.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.pretendard


@Composable
fun SearchNormalModeSection(
    modifier: Modifier,
    searchHistory: List<String>,
    deleteClick: (String) -> Unit,
    keywordClick: (String) -> Unit
) {

    Text(
        text = "최근 검색어", modifier = modifier, style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = pretendard,
            color = black1
        )
    )

    LazyColumn(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(searchHistory.size) { index ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) { keywordClick(searchHistory[index]) }
            ) {
                Text(
                    text = searchHistory[index], style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard,
                        color = black1
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            deleteClick(searchHistory[index])
                        }
                        .size(16.dp)
                )

            }
        }

    }


}