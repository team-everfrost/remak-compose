package com.everfrost.remak_compose.view.home.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white


@Composable
fun SummaryBoxWidget(
    summary: String
) {
    return Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .background(white, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = strokeGray2,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = summary,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendard,
                color = black1,
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp
            ),
            modifier = Modifier
        )
    }

}