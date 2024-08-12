package com.everfrost.remak_compose.view.account.widget.textfield

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black4
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.view.common.PasswordVisualTransformation

@Composable
fun CollectionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = black1,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = when {
                            isFocused && isError -> Color.Red
                            isFocused -> primaryBlue
                            else -> strokeGray2
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(start = 16.dp),
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = black4,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart)
                    )
                }
                innerTextField()
            }
        }
    )
}
