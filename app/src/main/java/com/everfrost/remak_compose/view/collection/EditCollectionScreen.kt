package com.everfrost.remak_compose.view.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.red1
import com.everfrost.remak_compose.view.account.widget.textfield.CollectionTextField
import com.everfrost.remak_compose.view.common.appbar.BackTitleAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.dialog.CustomConfirmDialog
import com.everfrost.remak_compose.viewModel.home.collection.CollectionViewModel

@Composable
fun EditCollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel,
    collectionName: String,
    collectionDescription: String
) {


    LaunchedEffect(true) {
        viewModel.setNewName(collectionName)
        if (collectionDescription == "no_description") {
            viewModel.setCollectionDescription("")
        } else {
            viewModel.setCollectionDescription(collectionDescription)
        }
        viewModel.setCollectionName(collectionName)
    }
    val isActionComplete by viewModel.isActionComplete.collectAsState()
    val newName by viewModel.newName.collectAsState()
    val description by viewModel.collectionDescription.collectAsState()

    when {
        isActionComplete == true -> {
            CustomConfirmDialog(
                onDismissRequest = {
                    viewModel.resetActionComplete()
                    navController.previousBackStackEntry?.savedStateHandle?.set("isUpdate", newName)
                    navController.popBackStack()
                },
                mainTitle = "컬렉션이 수정되었습니다",
                subTitle = "",
                btnText = "확인"
            )
        }

        isActionComplete == false -> {
            CustomConfirmDialog(
                onDismissRequest = {
                    viewModel.resetActionComplete()
                },
                mainTitle = "컬렉션 생성에 실패했습니다",
                subTitle = "중복된 이름입니다",
                btnText = "확인"
            )
        }
    }
    Scaffold(
        containerColor = bgGray2,
        topBar = {
            BackTitleAppBar(navController = navController, title = "새 컬렉션 만들기", color = bgGray2)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 48.dp)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = "컬렉션 이름", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black1
                        )
                    )
                    Text(
                        text = " *", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = red1,
                            textAlign = TextAlign.End
                        )
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                CollectionTextField(
                    value = newName,
                    onValueChange = {
                        viewModel.setNewName(it)
                    },
                    placeholder = "컬렉션 이름을 입력해주세요",
                    keyboardOptions = KeyboardOptions().copy(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = "설명", style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard,
                        color = black1
                    )
                )
                Spacer(modifier = Modifier.padding(8.dp))
                CollectionTextField(
                    value = description,
                    onValueChange = {
                        viewModel.setCollectionDescription(it)
                    },
                    placeholder = "컬렉션에 대한 설명을 입력해주세요",
                    keyboardOptions = KeyboardOptions(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = {
                        viewModel.updateCollection()
                    },
                    isEnable = collectionName.isNotEmpty(),
                    text = "수정하기"
                )
            }// Column
        }
    }


}