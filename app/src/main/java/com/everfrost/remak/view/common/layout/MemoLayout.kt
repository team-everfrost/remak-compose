package com.everfrost.remak.view.common.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.strokeGray2
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.tool.ViewTool


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemoLayout(
    modifier: Modifier,
    date: String,
    isSelected: Boolean,
    content: String,
    isEditMode: Boolean,
    onShortTab: () -> Unit,
    onLongTab: () -> Unit

) {
    val lines = content.split("\n")
    val firstPart = lines.firstOrNull() ?: ""
    val secondPart = if (lines.size > 1) {
        lines.subList(1, lines.size).joinToString("\n")
    } else {
        ""
    }
    Box(
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Transparent
            )
            .border(1.dp, strokeGray2, shape = RoundedCornerShape(16.dp))
            .background(white, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .combinedClickable(
                onClick = onShortTab,
                onLongClick = onLongTab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            if (isEditMode) {
                Image(
                    painter = if (isSelected) painterResource(id = R.drawable.icon_selected) else painterResource(
                        id = R.drawable.icon_unselected
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = firstPart,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = black1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard

                    )
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = secondPart,
                    maxLines = 3,
                    style = TextStyle(
                        color = black3,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard

                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "메모 | ${ViewTool.dateFormatting(date)}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = black3,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard

                    )
                )
            }

        }
    }
}