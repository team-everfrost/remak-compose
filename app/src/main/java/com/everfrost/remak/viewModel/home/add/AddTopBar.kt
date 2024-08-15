package com.everfrost.remak.viewModel.home.add

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(
    navController: NavController,
    modifier: Modifier
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = white,
        ),
        modifier = modifier,
        title = { },
        navigationIcon = {
            IconButton(
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
    )
}