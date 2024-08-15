package com.everfrost.remak_compose.view.account.onboarding.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue


@Composable
fun SmallRectWidget(
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .width(143.dp)
            .height(40.dp)

    )
    {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(37.dp)
                    .height(20.dp)
                    .background(color = Color(0xffCCE8FF), shape = RoundedCornerShape(8.dp)),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title, color = primaryBlue, style = TextStyle(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        fontSize = 11.sp,
                        fontFamily = pretendard,
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = description, color = Color.Black, style = TextStyle(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    fontSize = 11.sp,
                    fontFamily = pretendard,

                    )
            )

        }
    }

}