package com.everfrost.remak_compose.view.account.onboarding

import android.annotation.SuppressLint
import com.everfrost.remak_compose.view.account.onboarding.layout.AutoScrollingLazyRow
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.gray2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.primaryBlue
import com.everfrost.remak_compose.view.RemakScreen
import com.everfrost.remak_compose.view.account.onboarding.layout.LargeRecWidget
import com.everfrost.remak_compose.view.account.onboarding.layout.SmallRectWidget


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(
    navController: NavController
) {

    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .weight(0.64f)
                        .background(color = primaryBlue)
                ) {
                    Column {
                        Column(
                            modifier = Modifier.padding(
                                top = 50.dp,
                                start = 16.dp,
                            )
                        ) {
                            Text(
                                text = "쉽다, 빠르다, 편리하다",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                            Text(
                                text = "Remak",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 37.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = pretendard
                                )
                            )
                            Text(
                                modifier = Modifier.padding(top = 20.dp),
                                text = "자동 분류와 요약 추천까지 해주는\n편리한 글 읽기 제공 서비스",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = pretendard
                                )
                            )
                        }
                        Box(modifier = Modifier.padding(top = 43.dp))
                        AutoScrollingLazyRow(list = (1..8).take(4)) {
                            LargeRecWidget(title = "최신글.jpg", image = R.drawable.icon_picture)

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        AutoScrollingLazyRow(list = (1..8).take(4)) {
                            SmallRectWidget()
                        }


                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.36f)
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    Box(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 40.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = gray2,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate(RemakScreen.SignIn.route)

                        }
                        .height(65.dp)
                    )
                    {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_email),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp),
                                text = "이메일로 계속하기",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = pretendard
                                ),
                                color = Color.Black,
                            )

                        }
                    }
                }
            }
        }

    }
}

