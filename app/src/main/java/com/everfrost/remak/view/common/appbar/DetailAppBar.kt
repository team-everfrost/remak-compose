package com.everfrost.remak.view.common.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAppBar(
    backClick: () -> Unit,
    title: String,
    isShareEnable: Boolean,
    shareClick: () -> Unit,
    dropDownMenuContent: @Composable ((dismissMenu: () -> Unit) -> Unit),
    hasScrolled: Boolean = false,
    isEditMode: Boolean = false,
    onEditComplete: () -> Unit = {},
    color: Color = white

) {
    var isDropDownExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color,
        ),
        modifier = Modifier.then(
            if (hasScrolled) Modifier.drawBehind {
                drawLine(
                    color = black3,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1f
                )
            }
            else Modifier
        ),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp),
                onClick = {
                    backClick()
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_back),
                        contentDescription = "back"
                    )
                }
            )
        },
        title = {
            Text(
                modifier = Modifier,
                text = title,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        actions = {
            if (isShareEnable) {
                IconButton(
                    onClick = {
                        shareClick()
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_share),
                            contentDescription = "share"
                        )
                    }
                )
            }
            if (isEditMode) {
                Text(
                    text = "완료",
                    Modifier
                        .padding(end = 16.dp)
                        .clickable(
                            //ripple제거
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onEditComplete()

                        },
                    style = TextStyle(
                        color = black1,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard
                    )
                )

            } else {
                IconButton(
                    onClick = {
                        isDropDownExpanded = true
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_more),
                            contentDescription = "more"
                        )
                    }
                )

                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(
                        extraSmall = RoundedCornerShape(12.dp)
                    )
                ) {
                    DropdownMenu(
                        expanded = isDropDownExpanded,
                        onDismissRequest = { isDropDownExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        dropDownMenuContent {
                            isDropDownExpanded = false
                        }
                    }
                }

            }
        }
    )
}