package com.everfrost.remak_compose.view.home.detail.memo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.appbar.DetailAppBar
import com.everfrost.remak_compose.view.common.dialog.CustomSelectDialog
import com.everfrost.remak_compose.view.common.textField.RoundGrayTextField
import com.everfrost.remak_compose.viewModel.home.detail.memo.MemoDetailViewModel

@Composable
fun MemoDetailScreen(
    docIdx: String? = "aa38f544-a43d-47ec-8798-0de378c47276",
    viewModel: MemoDetailViewModel,
    navController: NavController
) {

    LaunchedEffect(true) {
        viewModel.fetchDetailData(docIdx!!)
    }


    val isEditMode by viewModel.isEditMode.collectAsState()
    val date by viewModel.date.collectAsState()
    val memo by viewModel.memo.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val editCancelDialog by viewModel.editCancelDialog.collectAsState()

    when {
        editCancelDialog ->
            CustomSelectDialog(
                onDismissRequest = {
                    viewModel.toggleEditCancelDialog()
                },
                onConfirm = {
                    viewModel.setMemo(viewModel.initialMemo.value)
                    viewModel.changeEditMode(false)
                    viewModel.toggleEditCancelDialog()
                    focusManager.clearFocus()
                },
                mainTitle = "수정을 취소하시겠습니까?",
                subTitle = "작성한 내용이 지워져요",
                confirmBtnText = "네",
                cancelBtnText = "아니오"
            )
    }

    BackHandler {
        if (isEditMode) {
            viewModel.toggleEditCancelDialog()
        } else {
            navController.popBackStack()

        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            DetailAppBar(
                onEditComplete = {
                    viewModel.updateMemo(docIdx!!)
                    focusManager.clearFocus()

                },
                isEditMode = isEditMode,
                backClick = { /*TODO*/ },
                title = "메모",
                isShareEnable = false,
                shareClick = { },
                dropDownMenuContent = {
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "편집하기", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                        }
                    )
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = date, style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        color = black3
                    )
                )

                RoundGrayTextField(
                    focusRequester = focusRequester,
                    value = memo,
                    onValueChange = {
                        viewModel.setMemo(it)
                    },
                    placeholder = "",
                    keyboardOptions = KeyboardOptions(),
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(500.dp),
                    onFocusChange = {
                        if (it) {
                            viewModel.changeEditMode(true)
                        }
                    }
                )

            }
        }


    }

}