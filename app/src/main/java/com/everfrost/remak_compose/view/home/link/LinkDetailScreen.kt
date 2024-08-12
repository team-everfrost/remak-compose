package com.everfrost.remak_compose.view.home.link

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray1
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.bgGray4
import com.everfrost.remak_compose.ui.theme.black1
import com.everfrost.remak_compose.ui.theme.black3
import com.everfrost.remak_compose.ui.theme.gray3
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.ui.theme.strokeGray2
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.common.appbar.DetailAppBar
import com.everfrost.remak_compose.view.common.button.PrimaryButton
import com.everfrost.remak_compose.view.common.layout.TagRowLayout
import com.everfrost.remak_compose.viewModel.home.link.LinkDetailViewModel


@Composable
fun LinkDetailScreen(
    docIdx: String? = "11a6258e-c933-4d6d-bde5-e65b2a389734",
    navController: NavController,
    viewModel: LinkDetailViewModel
) {
    val getDetailDataState by viewModel.getDetailDataState.collectAsState()
    val linkData by viewModel.linkData.collectAsState()
    val date by viewModel.date.collectAsState()
    val title by viewModel.title.collectAsState()
    val tagList by viewModel.tagList.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val url by viewModel.url.collectAsState()
    val scrollState = rememberScrollState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()
    val context = LocalContext.current


    val webView = remember(linkData) {
        if (!linkData.isNullOrEmpty()) {
            WebView(context).apply {
                setupWebView()
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        viewModel.dataLoaded()
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url.toString()
                        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(ContextCompat.getColor(context, R.color.black))
                            .build()
                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setDefaultColorSchemeParams(colorSchemeParams)
                            .build()
                        customTabsIntent.launchUrl(context, Uri.parse(url))
                        return true
                    }
                }
                loadHtmlData(linkData)
                setOnLongClickListener {
                    val hitTestResult = hitTestResult
                    if (hitTestResult.type == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
                    ) {
                        val imageUrl = hitTestResult.extra!!
                        val fileName = hitTestResult.extra!!.substringAfterLast("/")
                        Log.d("fileName", fileName)
                        // Todo : 이미지 다운로드 및 공유 기능 추가
//                        UtilityDialog.showImageDialog(
//                            context,
//                            downloadBtnClick = {
//                                downloadImage(imageUrl)
//                            },
//                            shareBtnClick = {
//                                lifecycleScope.launch {
//                                    viewModel.downloadAndShareImage(context, imageUrl, fileName)
//                                }
//                            },
//                            selfShareBtnClick = {
//                                lifecycleScope.launch {
//                                    viewModel.shareSelf(context, imageUrl, fileName)
//                                }
//
//                            }
//                        )
                    }
                    false

                }
            }
        } else {
            null // linkData가 비어 있을 경우 null 반환
        }
    }


    LaunchedEffect(true) {
        viewModel.fetchDetailData(docIdx!!)
    }
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            DetailAppBar(
                hasScrolled = hasScrolled,
                backClick = { /*TODO*/ },
                title = "링크",
                isShareEnable = false,
                shareClick = { },
                dropDownMenuContent = {
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "편집하기", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                        }
                    )
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            if (isDataLoaded) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Row(
                        modifier = Modifier.padding(top = 32.dp)

                    ) {
                        Text(
                            text = date,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = pretendard,
                                color = black3,
                                fontWeight = FontWeight.Normal
                            ),
                        )
                        Text(
                            text = url, style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = pretendard,
                                color = bgGray4,
                                fontWeight = FontWeight.Medium,
                            ),
                            maxLines = 1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Text(
                        text = title, style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = pretendard,
                            color = black1,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    )


                    TagRowLayout(
                        modifier = Modifier.padding(top = 24.dp),
                        tags = tagList,
                        onClick = { }
                    )

                    Text(
                        text = "요약", style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = pretendard,
                            color = black1,
                            fontWeight = FontWeight.Bold
                        ), modifier = Modifier.padding(top = 24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                            .background(white, shape = RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = strokeGray2,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = summary,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = pretendard,
                                color = black1,
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier
                        )
                    }
                    Box(modifier = Modifier.height(16.dp))

                    AndroidView(
                        factory = {
                            webView!!
                        },

                        )

                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        onClick = {
                            val colorSchemeParams = CustomTabColorSchemeParams.Builder()
                                .setToolbarColor(ContextCompat.getColor(context, R.color.black))
                                .build()
                            val customTabsIntent = CustomTabsIntent.Builder()
                                .setDefaultColorSchemeParams(colorSchemeParams)
                                .build()
                            customTabsIntent.launchUrl(context, Uri.parse(url))

                        },
                        isEnable = true,
                        text = "웹 페이지로 이동하기"
                    )


                } // Column
//            }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.setupWebView() {
    webChromeClient = WebChromeClient()
    overScrollMode = WebView.OVER_SCROLL_NEVER
    isHorizontalScrollBarEnabled = false
    isVerticalScrollBarEnabled = false
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
}

private fun WebView.loadHtmlData(linkData: String) {
    val css = """ 
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/styles/default.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/highlight.min.js"></script>
<script>hljs.highlightAll();</script>    
<style type='text/css'>
body {
    font-weight: 400; 
    line-height: 1.6; 
    max-width: 100%;
    overflow-x: hidden;
    word-wrap: break-word;
    word-break: break-all;
}
img {
    max-width: 100%;
    height: auto;
}
pre {
    white-space: pre-wrap;
}
p {
    margin-top: 0;
    margin-bottom: 0;
}
table {
    border-collapse: collapse;
    width: 100%;
}
th, td {
    border: 1px solid black;
    padding: 8px;
}
</style>
"""

    val htmlData = """
    <html>
    <head>
        $css
    </head>
    <body>
    $linkData
    </body>
    </html>
    """.trimIndent()
    loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
}

