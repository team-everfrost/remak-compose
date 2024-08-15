package com.everfrost.remak.view.common.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.ui.theme.bgGray2
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.strokeGray2

@Composable
fun RoundGrayTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    isEnable: Boolean = true,
    onFocusChange: (Boolean) -> Unit = {}, // 포커스 상태 변화 콜백,
    focusRequester: FocusRequester = FocusRequester()
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        enabled = isEnable,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChange(isFocused)
            },

        textStyle = TextStyle(
            fontSize = 14.sp,
            color = black1,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(bgGray2, RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = strokeGray2,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(start = 16.dp, top = 20.dp),
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = black3,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                innerTextField()
            }
        }
    )
}