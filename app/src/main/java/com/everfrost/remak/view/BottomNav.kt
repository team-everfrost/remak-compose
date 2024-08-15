package com.everfrost.remak.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.everfrost.remak.ui.theme.bgGray2
import com.everfrost.remak.ui.theme.black2
import com.everfrost.remak.ui.theme.black4
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.white

@Composable
fun BottomNav(navController: NavController) {
    val items = listOf(
        RemakScreen.Main,
        RemakScreen.Search,
        RemakScreen.Tag,
        RemakScreen.Collection,
        RemakScreen.Profile,
    )

    BottomNavigation(
        backgroundColor = white,
        modifier = Modifier
            .background(bgGray2)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .shadow(
                elevation = 1.dp,
                spotColor = Color(0x05414A52),
                ambientColor = Color(0x05414A52)
            )
            .navigationBarsPadding()
            .fillMaxWidth()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            navController.graph.startDestinationRoute?.let { startRoute ->
                                popUpTo(startRoute) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            painter = painterResource(id = screen.icon!!),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        fontFamily = pretendard,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                },
                selectedContentColor = black2,
                unselectedContentColor = black4,
            )
        }
    }
}
