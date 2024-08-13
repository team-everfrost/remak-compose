package com.everfrost.remak_compose.view.collection

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.viewModel.home.collection.CollectionViewModel
import com.everfrost.remak_compose.viewModel.home.tag.TagViewModel
import com.skydoves.landscapist.glide.GlideImage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CollectionBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    viewModel: CollectionViewModel,
    selectedDocument: List<String>,
    onConfirm: () -> Unit

) {

    LaunchedEffect(true) {
        viewModel.fetchCollectionList()
    }

    val collectionList by viewModel.collectionList.collectAsState()
    val isAnyChecked by viewModel.isAnyChecked.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = white,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .navigationBarsPadding()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .background(white)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "컬렉션 추가",
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = black1
                    ),
                )
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onDismissRequest()
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(collectionList.size) { index ->
                    CollectionBottomSheetItem(
                        name = collectionList[index].name,
                        isSelected = collectionList[index].isSelected,
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.toggleCollectionListCheck(index)
                        }
                    )
                }

            }

            PrimaryButton(
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .height(60.dp), onClick =
                {
                    viewModel.addDataInCollection(selectedDocument)
                    onConfirm()
                    onDismissRequest()
                }, isEnable = isAnyChecked,
                text = "확인"
            )
        }
    }

}

@Composable
fun CollectionBottomSheetItem(
    name: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit = { }
) {

    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = name, style = TextStyle(
                fontFamily = pretendard,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = black2
            )
        )
        GlideImage(imageModel = {
            if (isSelected) R.drawable.icon_selected else R.drawable.icon_unselected_check
        }, modifier = Modifier.size(20.dp))


    }

}