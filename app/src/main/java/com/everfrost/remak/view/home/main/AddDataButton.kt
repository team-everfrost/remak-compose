package com.everfrost.remak.view.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.everfrost.remak.ui.theme.strokeGray2
import com.everfrost.remak.ui.theme.white

@Composable
fun AddDataButton(
    modifier: Modifier,
    onClick: () -> Unit,
    title: String,
    textStyle: TextStyle
) {
    Box(
        modifier = modifier
            .border(width = 1.dp, color = strokeGray2, shape = RoundedCornerShape(12.dp))
            .background(white, shape = RoundedCornerShape(12.dp))
            .clickable(
                //ripple 효과 제거
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
    ) {
        Text(text = title, style = textStyle, modifier = Modifier.align(Alignment.Center))
    }

}