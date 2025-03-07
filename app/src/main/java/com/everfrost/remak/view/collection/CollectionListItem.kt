package com.everfrost.remak.view.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.ui.theme.black2
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.primaryBlue
import com.everfrost.remak.ui.theme.strokeGray2
import com.everfrost.remak.ui.theme.white


@Composable
fun CollectionListItem(
    modifier: Modifier,
    collectionName: String,
    collectionDescription: String,
    collectionCount: Int
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Transparent
            )
            .border(1.dp, strokeGray2, shape = RoundedCornerShape(16.dp))
            .background(white, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp, bottom = 14.dp)
    ) {

        Column {
            Text(
                text = collectionName, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = pretendard,
                    color = black2
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = collectionDescription, style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = pretendard,
                    color = black3
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${collectionCount}개", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendard,
                    color = if (collectionCount == 0) black3 else
                        primaryBlue
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }


    }


}