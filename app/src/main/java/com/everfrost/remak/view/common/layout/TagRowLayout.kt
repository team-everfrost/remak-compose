package com.everfrost.remak.view.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.strokeGray2
import com.everfrost.remak.ui.theme.white

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagRowLayout(
    tags: List<String>,
    onClick: (Int) -> Unit,
    modifier: Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),

        ) {
        tags.forEachIndexed { index, tag ->
            TagBox(
                tag = tag,
                onClick = {
                    onClick(index)
                }
            )
        }

    }


}

@Composable
fun TagBox(
    tag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(35.dp)
            .background(white)
            .border(1.dp, strokeGray2, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp)
            .clickable(
                //ripple제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },

        ) {
        Text(
            text = "#${tag}", style = TextStyle(
                fontSize = 14.sp,
                color = black3,
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.align(Alignment.Center)
        )


    }


}