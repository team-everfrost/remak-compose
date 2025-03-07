package com.everfrost.remak.view.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.everfrost.remak.ui.theme.pretendard

@Composable
fun CustomSelectDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    mainTitle: String,
    subTitle: String,
    confirmBtnText: String,
    cancelBtnText: String,
    isBtnBlue: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
            ) {
                Text(
                    text =
                    mainTitle,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subTitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xff757779)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .height(52.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xffF4F6F8),
                        )
                    ) {
                        Text(
                            text = cancelBtnText,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))


                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .height(52.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = if (isBtnBlue) ButtonDefaults.buttonColors(
                            containerColor = Color(0xff1F8CE6),
                        ) else ButtonDefaults.buttonColors(
                            containerColor = Color(0xffF83A41),
                        )
                    ) {
                        Text(
                            text = confirmBtnText,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } // column
        }
    }
}