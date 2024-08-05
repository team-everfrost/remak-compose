package com.everfrost.remak_compose.view.home.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.white
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MainAppBar(
    modifier: Modifier = Modifier,
    background: Color = white,
    scrollUpState: Boolean,
    onAddClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    val position by animateFloatAsState(if (scrollUpState == true) -180f else 0f)

    Surface(modifier = Modifier.graphicsLayer { translationY = (position) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(color = background),
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                GlideImage(imageModel = { R.drawable.image_logo })

                Row {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_add),
                            contentDescription = null,
                            tint = black1
                        )
                    }
                    Spacer(modifier = Modifier.size(14.dp))
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_more),
                            contentDescription = null,
                            tint = black1
                        )
                    }

                }

            }
        }

    }
}