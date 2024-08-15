@file:OptIn(ExperimentalMaterial3Api::class)

package com.everfrost.remak.view.common.appbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white


@Composable
fun BackTitleAppBar(
    navController: NavController,
    title: String,
    color: Color = white
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color,
        ),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp),
                onClick = {
                    navController.popBackStack()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    )
}