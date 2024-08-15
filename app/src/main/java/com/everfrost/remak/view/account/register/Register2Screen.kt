import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.ui.theme.bgGray2
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.primaryBlue
import com.everfrost.remak.ui.theme.red1
import com.everfrost.remak.ui.theme.strokeGray2
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.RemakScreen
import com.everfrost.remak.view.common.appbar.BackTitleAppBar
import com.everfrost.remak.viewModel.account.register.RegisterViewModel

@Composable
fun Register2Screen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    )
    val codeValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(6) {
        remember {
            FocusRequester()
        }
    }
    val email by viewModel.email.collectAsState()
    val isVerifySuccess by viewModel.isVerifySuccess.collectAsState()

    LaunchedEffect(true) {
        focusRequesters[0].requestFocus()
    }

    LaunchedEffect(isVerifySuccess) {
        if (isVerifySuccess == true) {
            // 인증번호가 일치할 때
            viewModel.setIsVerifySuccess(null)
            navController.navigate(RemakScreen.Register3.route)
        }

    }

    Scaffold(
        containerColor = white,
        topBar = {
            BackTitleAppBar(
                navController = navController,
                title = "회원가입"
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp)
            ) {

                if (isVerifySuccess == null || isVerifySuccess == true) {
                    Row {
                        Text(
                            text = email,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = pretendard,
                                color = primaryBlue
                            )
                        )
                        Text(
                            text = "로 발송된",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = pretendard,
                                color = black1
                            )
                        )
                    }
                    Text(
                        text = "인증번호를 입력해주세요",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black1
                        )
                    )
                } else {
                    Text(
                        text = "인증번호가 일치하지 않습니다\n다시 확인해주세요",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = red1
                        )
                    )
                }


                Row(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    codeValues.forEachIndexed { index, value ->
                        CodeTextField(
                            customTextSelectionColors = customTextSelectionColors,
                            codeValue = codeValues[index],
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    codeValues[index] = newValue
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                                if (codeValues.all { it.isNotEmpty() }) {
                                    val verifyCode = codeValues.joinToString("")
                                    viewModel.checkVerifyCode(verifyCode)
                                }
                            },
                            onBackspace = {
                                Log.d("Register2Screen", "onBackspace: $index")
                                codeValues[index] = ""
                                if (index > 0 && codeValues[index].isEmpty()) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            modifier = Modifier
                                .focusRequester(focusRequesters[index]),
                            isError = isVerifySuccess == false
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun CodeTextField(
    customTextSelectionColors: TextSelectionColors,
    codeValue: String = "",
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier,
    isError: Boolean,
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        BasicTextField(
            value = codeValue,
            onValueChange = onValueChange,
            cursorBrush = SolidColor(Color.Transparent),
            modifier = modifier
                .width(48.dp)
                .height(52.dp)
                .onKeyEvent {
                    if (it.key.keyCode == 287762808832) {
                        onBackspace()
                        true
                    } else {
                        false
                    }
                },
            textStyle = TextStyle(
                fontSize = 28.sp,
                color = if (isError) {
                    Color.Red
                } else {
                    black1
                },
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.None,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(bgGray2, RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = if (isError) {
                                Color.Red
                            } else {
                                strokeGray2

                            },
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
    }
}
