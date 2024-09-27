package com.everfrost.remak.view.home.detail.link

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.everfrost.remak.R
import com.everfrost.remak.ui.theme.bgGray4
import com.everfrost.remak.ui.theme.black1
import com.everfrost.remak.ui.theme.black3
import com.everfrost.remak.ui.theme.pretendard
import com.everfrost.remak.ui.theme.red1
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.collection.CollectionBottomSheet
import com.everfrost.remak.view.common.appbar.DetailAppBar
import com.everfrost.remak.view.common.button.PrimaryButton
import com.everfrost.remak.view.common.dialog.CustomImageDialog
import com.everfrost.remak.view.common.dialog.CustomSelectDialog
import com.everfrost.remak.view.common.layout.TagRowLayout
import com.everfrost.remak.view.home.detail.SummaryBoxWidget
import com.everfrost.remak.viewModel.collection.CollectionViewModel
import com.everfrost.remak.viewModel.home.detail.link.LinkDetailViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkDetailScreen(
    docIdx: String?,
    navController: NavController,
    viewModel: LinkDetailViewModel,
    collectionViewModel: CollectionViewModel
) {

    val scope = rememberCoroutineScope()
    val getDetailDataState by viewModel.getDetailDataState.collectAsState()
    val linkData by viewModel.linkData.collectAsState()
    val date by viewModel.date.collectAsState()
    val title by viewModel.title.collectAsState()
    val tagList by viewModel.tagList.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val url by viewModel.url.collectAsState()
    val scrollState = rememberScrollState()
    val isDeleteComplete by viewModel.isDeleteComplete.collectAsState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }
    var collectionBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()
    val context = LocalContext.current
    var deleteDialog by remember { mutableStateOf(false) }
    var imageDialog by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    val isSelfShareSuccess by viewModel.isSelfShareSuccess.collectAsState()
    var isWebViewLoaded by remember { mutableStateOf(false) }


    val webView = remember(linkData) {
        if (linkData.isNotEmpty()) {
            WebView(context).apply {

                alpha = 0.99F
                setupWebView()
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        viewModel.dataLoaded()
                        isWebViewLoaded = true
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
                        imageUrl = hitTestResult.extra!!
                        Log.d("imageUrl", imageUrl)
                        imageDialog = true
                    }
                    false
                }
            }
        } else {
            null
        }
    }


    LaunchedEffect(true) {
        viewModel.fetchDetailData(docIdx!!)
    }

    LaunchedEffect(isDeleteComplete) {
        if (isDeleteComplete) {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "isUpdate",
                true
            )
            navController.popBackStack()
        }
    }

    when {
        isSelfShareSuccess ->
            if (isSelfShareSuccess) {
                Toast.makeText(context, "파일을 저장되었습니다.", Toast.LENGTH_SHORT).show()
                viewModel.setIsSelfShareSuccess(false)

            } else {
                Toast.makeText(context, "파일저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                viewModel.setIsSelfShareSuccess(false)

            }
    }

    when {
        deleteDialog ->
            CustomSelectDialog(
                onDismissRequest = {
                    deleteDialog = false
                },
                onConfirm = {
                    viewModel.deleteDocument(docIdx!!)
                    deleteDialog = false
                },
                mainTitle = "링크를 삭제하시겠습니까?",
                subTitle = "삭제시 복구가 불가능해요",
                confirmBtnText = "삭제하기",
                cancelBtnText = "취소하기"
            )
    }

    when {
        imageDialog ->
            CustomImageDialog(
                onDismissRequest = {
                    imageDialog = false
                },
                onDownloadClick = {
                    viewModel.downloadImage(imageUrl, context)
                    imageDialog = false
                },
                onSelfShareClick = {
                    scope.launch {
                        viewModel.shareSelf(context, imageUrl)
                    }
                    imageDialog = false

                },
                onOtherShareClick = {
                    scope.launch {
                        viewModel.downloadAndShareImage(
                            context,
                            imageUrl,
                            URLUtil.guessFileName(imageUrl, null, null)
                        )
                    }
                    imageDialog = false
                },
            )

    }
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = white,
        topBar = {
            DetailAppBar(
                hasScrolled = hasScrolled,
                backClick = {
                    navController.popBackStack()
                },
                title = "링크",
                isShareEnable = false,
                shareClick = { },
                dropDownMenuContent = { dismissMenu ->
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "컬렉션에 추가", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                            dismissMenu()
                            collectionBottomSheet = true

                        }
                    )
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "공유", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },

                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, url)
                                type = "text/plain"
                            }
                            context.startActivity(
                                Intent.createChooser(shareIntent, "링크 공유하기")
                            )

                        }
                    )
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "브라우저 보기", style = TextStyle(
                                    color = black1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            context.startActivity(i)
                            dismissMenu()
                        }
                    )
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            Text(
                                "삭제", style = TextStyle(
                                    color = red1,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = pretendard
                                )
                            )
                        },
                        onClick = {
                            deleteDialog = true
                            dismissMenu()
                        }
                    )
                }
            )
        },
    ) { innerPadding ->
        if (collectionBottomSheet) {
            CollectionBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        collectionBottomSheet = false
                    }
                }, sheetState =
                sheetState,
                collectionViewModel,
                listOf(docIdx!!),
                onConfirm = {}
            )
        }
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
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                browserView(context, url)
                            }
                    )


                    TagRowLayout(
                        modifier = Modifier.padding(top = 24.dp),
                        tags = tagList,
                        onClick = {
                            navController.navigate("TagDetail/${tagList[it]}")
                        }
                    )

                    Text(
                        text = "요약", style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = pretendard,
                            color = black1,
                            fontWeight = FontWeight.Bold
                        ), modifier = Modifier.padding(top = 24.dp)
                    )

                    SummaryBoxWidget(summary = summary)

                    Box(modifier = Modifier.height(16.dp))

                    if (webView != null) {
                        AndroidView(
                            modifier = Modifier.alpha(0.99f),
                            factory = {
                                webView
                            },
                        )
                    }

                    if (isWebViewLoaded) {
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
                    }

                    Box(modifier = Modifier.height(16.dp))

                } // Column
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

private fun browserView(
    context: Context,
    url: String
) {
    val colorSchemeParams = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(ContextCompat.getColor(context, R.color.black))
        .build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(colorSchemeParams)
        .build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}


