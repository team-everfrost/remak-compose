package com.everfrost.remak_compose.view.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.ui.theme.black4
import com.everfrost.remak_compose.ui.theme.gray3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue
import com.everfrost.remak_compose.ui.theme.white

@Composable
fun PrimaryButton(
    modifier: Modifier,
    onClick: () -> Unit,
    isEnable: Boolean,
    text: String
) {
    Box(
        modifier = modifier
            .background(
                color = if (isEnable) primaryBlue else
                    gray3, shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled = isEnable,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = TextStyle(
                color = if (isEnable) white else black4,
                fontSize = 18.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold
            )
        )


    }


}