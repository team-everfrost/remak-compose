package com.everfrost.remak_compose.view.account.onboarding.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue


@Composable
fun LargeRecWidget(
    title: String,
    image: Int,
) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .width(108.dp)
            .height(124.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .size(69.dp, 20.dp)
                    .background(Color(0xffCCE8FF), shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "test", color = primaryBlue, style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(60.dp, 60.dp)
                    .background(Color(0xffF4F6F8), shape = RoundedCornerShape(8.dp))
                    .align(Alignment.End)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )

            }
        }

    }
}
