package com.everfrost.remak_compose.view.profile

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black2
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.common.button.GrayButton
import com.everfrost.remak_compose.viewModel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {

    val context = LocalContext.current
    val storageSize by viewModel.storageSize.collectAsState()
    val usagePercentage by viewModel.usagePercentage.collectAsState()
    val storageUsage by viewModel.storageUsage.collectAsState()

    LaunchedEffect(true) {
        viewModel.getStorageSize()
    }

    Scaffold(
        containerColor = bgGray2,
        bottomBar = {
            BottomNav(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 40.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row {
                    Text(
                        text = "내 정보", style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = pretendard
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    GrayButton(
                        modifier = Modifier
                            .width(81.dp)
                            .height(30.dp),
                        onClick = {
                            navController.navigate(RemakScreen.AddCollection.route)
                        },
                        text = "프로필 편집"
                    )

                }
                UsageSection(
                    current = storageUsage,
                    total = storageSize.toInt(),
                    percentage = usagePercentage,
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                    unit = "GB",
                    role = "무료 플랜"
                )

                PrivacyAndPolicySection(
                    title = "이용약관",
                    onClick = {
                        viewModel.openBrowser("https://remak.io/terms-of-service", context)
                    },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(strokeGray2)
                )
                PrivacyAndPolicySection(
                    title = "개인정보 처리방침",
                    onClick = {
                        viewModel.openBrowser("https://remak.io/privacy-policy", context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(strokeGray2)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .fillMaxSize(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "앱 버전",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black2
                        )
                    )
                    Text(
                        text = getAppVersionInfo(context),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = pretendard,
                            color = black3
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(strokeGray2)
                )

                RemakLogoSection(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(70.dp)
                )
            }
        }
    }
}

@Composable
fun UsageSection(
    current: Double,
    total: Int,
    unit: String,
    percentage: Int,
    role: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, strokeGray2, RoundedCornerShape(12.dp))
            .background(white, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = role,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard,
                        color = black3
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$current/$total${unit} (${percentage}%) 사용 중",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard,
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = black1,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun PrivacyAndPolicySection(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = pretendard,
                color = black2
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = null,
            tint = black1,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun RemakLogoSection(
    modifier: Modifier
) {
    Box(modifier = modifier.background(primaryBlue, shape = RoundedCornerShape(12.dp))) {
        Row(
            modifier = Modifier
                .padding(start = 7.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_remak),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Column {
                Text(
                    text = "Remak을 웹, 확장프로그램에서도!", style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendard,
                        color = white
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "remak.io", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = pretendard,
                            color = white
                        )
                    )
                    Text(
                        text = " 에서 이용해보세요.", style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = pretendard,
                            color = white
                        )
                    )
                }

            }
        }
    }

}

fun getAppVersionInfo(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName ?: "Unknown"
        versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}

