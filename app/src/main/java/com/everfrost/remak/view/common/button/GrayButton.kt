package com.everfrost.remak.view.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.ui.theme.bgBlueGray1
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white

@Composable
fun GrayButton(
    modifier: Modifier,
    onClick: () -> Unit,
    text: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                color = bgBlueGray1
            )
            .clickable(
                //ripple 효과 제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = TextStyle(
                color = white,
                fontSize = 14.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium
            )
        )


    }


}