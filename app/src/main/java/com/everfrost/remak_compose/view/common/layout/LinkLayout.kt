package com.everfrost.remak_compose.view.common.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.tool.ViewTool
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinkLayout(
    modifier: Modifier,
    title: String,
    url: String,
    summary: String?,
    status: String,
    isSelected: Boolean,
    isEditMode: Boolean,
    thumbnailUrl: String?,
    onShortTab: () -> Unit,
    onLongTab: () -> Unit
) {
    val newTitle = title.replace(" ", "")
    Box(
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Transparent
            )
            .border(1.dp, strokeGray2, shape = RoundedCornerShape(16.dp))
            .background(white, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .combinedClickable(
                onClick = onShortTab,
                onLongClick = onLongTab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (isEditMode) {
                Image(
                    painter = if (isSelected) painterResource(id = R.drawable.icon_selected) else painterResource(
                        id = R.drawable.icon_unselected
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (newTitle.isEmpty()) url else title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = black1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = pretendard

                    )
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = when (status) {
                        "SCRAPE_PENDING" -> "스크랩 대기중이에요."
                        "SCRAPE_PROCESSING" -> "스크랩이 진행중이에요!"
                        "SCRAPE_REJECTED" -> "스크랩에 실패했어요."
                        "EMBED_PENDING" -> "AI가 곧 자료를 요약할거에요."
                        "EMBED_PROCESSING" -> "AI가 자료를 요약중이에요!"
                        "EMBED_REJECTED" -> "AI가 자료를 요약하지 못했어요."
                        "COMPLETED" -> {
                            if (summary!!.contains("\n")) {
                                val index = summary.indexOf("\n")
                                summary.substring(0, index)
                            } else {
                                summary!!
                            }
                        }

                        else -> ""

                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = black3,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard

                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "링크 | ${ViewTool.extractDomain(url)}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = black3,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = pretendard

                    )
                )
            }
            if (thumbnailUrl.isNullOrEmpty()) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(80.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.image_empty_image),
                    contentDescription = null,
                )
            } else {
                GlideImage(
                    imageModel = { thumbnailUrl },
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 2.dp,
                            color = strokeGray2,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    requestOptions = {
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    },
                )
            }
        }
    }
}