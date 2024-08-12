package com.everfrost.remak_compose.view.common.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.bgGray4
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.gray3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAppBar(
    backClick: () -> Unit,
    title: String,
    isShareEnable: Boolean,
    shareClick: () -> Unit,
    dropDownMenuContent: @Composable (() -> Unit),
    hasScrolled: Boolean = false

) {
    var isDropDownExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = white,
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

            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                dropDownMenuContent()
            }
        }
    )
}