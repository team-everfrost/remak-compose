package com.everfrost.remak.view.common.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.everfrost.remak.ui.theme.pretendard

@Composable
fun CustomImageDialog(
    onDismissRequest: () -> Unit,
    onDownloadClick: () -> Unit,
    onSelfShareClick: () -> Unit,
    onOtherShareClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    text = "이미지", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Box(modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onDownloadClick()
                    }
                    .padding(horizontal = 16.dp)) {
                    Text(
                        text = "이미지 다운로드", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.align(Alignment.CenterStart)

                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onSelfShareClick()
                    }
                    .padding(horizontal = 16.dp)) {
                    Text(
                        text = "Remak에 이미지 추가", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.align(Alignment.CenterStart)

                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onOtherShareClick()
                    }
                    .padding(horizontal = 16.dp)) {
                    Text(
                        text = "이미지 외부 공유", style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }
    }
}