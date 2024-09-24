package com.everfrost.remak

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.everfrost.remak.ui.theme.Remak_composeTheme
import com.everfrost.remak.ui.theme.white
import com.everfrost.remak.view.RemakApp
import com.everfrost.remak.view.common.dialog.CustomShareDialog
import com.everfrost.remak.viewModel.SplashViewModel
import com.everfrost.remak.viewModel.home.add.AddViewModel
import com.everfrost.remak.viewModel.home.main.HomeMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val addViewModel: AddViewModel by viewModels()
    private val homeMainViewModel: HomeMainViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()

    private var showShareDialog by mutableStateOf(false)
    private var sharedText by mutableStateOf("")
    private var sharedUrl by mutableStateOf("")
    private var combinedText by mutableStateOf("")

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingShare(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SplashScreen", "onCreate")
        installSplashScreen().setKeepOnScreenCondition {
            Log.d("SplashScreen", "isReady: ${splashViewModel.isReady.value}")
            !splashViewModel.isReady.value
        }
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { //폴더블 상태 변경 시 onCreate가 다시 호출되는데, 이 때 재 저장 방지
            handleIncomingShare(intent)
        }

        enableEdgeToEdge() // 전체화면 모드
        lifecycleScope.launch {
            addViewModel.isActionComplete.collect { isComplete ->
                if (isComplete) {
                    Toast.makeText(this@MainActivity, "Remak에 저장했습니다", Toast.LENGTH_SHORT)
                        .show()
                    delay(200); // 딜레이추가로 새로고침 구현
                    homeMainViewModel.resetMainList()
                    homeMainViewModel.fetchMainList()
                    addViewModel.setIsActionComplete(false)
                }
            }
        }

        setContent {
            val startDestination by splashViewModel.screen.collectAsState()
            if (splashViewModel.isReady.collectAsState().value) {
                Remak_composeTheme(
                    darkTheme = false
                ) {
                    Surface(
                        color = white
                    ) {
                        RemakApp(
                            startDestination = startDestination,
                            homeMainViewModel = homeMainViewModel
                        )

                        if (showShareDialog) {
                            CustomShareDialog(
                                onDismissRequest = { showShareDialog = false },
                                onMemoClick = {
                                    addViewModel.addShareMemo(combinedText)
                                    showShareDialog = false
                                },
                                onLinkClick = {
                                    addViewModel.createShareWebPage(sharedUrl)
                                    showShareDialog = false
                                },
                                mainTitle = "공유 텍스트 저장",
                                subTitle = "이 텍스트를 어떻게 저장할까요?",
                                linkBtnText = "링크로 저장",
                                memoBtnText = "메모로 저장"
                            )
                        }
                    }
                }
            }
        }
    }


    private fun handleIncomingShare(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            when {
                type.startsWith("image/") -> {
                    handleFile(intent)
                }

                type == "text/plain" -> {
                    val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if (sharedText != null) {
                        val (text, url, all) = extractTextAndUrl(sharedText)
                        when {
                            url.isNotEmpty() && text.isEmpty() -> {
                                addViewModel.createShareWebPage(url)
                            }

                            url.isEmpty() && text.isNotEmpty() -> {
                                addViewModel.addShareMemo(text)
                            }

                            url.isNotEmpty() && text.isNotEmpty() -> {
                                this.sharedUrl = url
                                this.sharedText = text
                                this.combinedText = all
                                showShareDialog = true
                            }
                        }
                    }
                }

                type.startsWith("application/") || type.startsWith("audio/") || type.startsWith("video/") || type == "*/*" -> {
                    handleFile(intent)
                }

                else -> {
                    // Handle other types of data here if necessary
                }

            }
            homeMainViewModel.resetMainList()
            homeMainViewModel.fetchMainList()

        }
    }


    private fun handleText(intent: Intent) {
        val urlRegex = """^(http[s]?://)?[^\s[("<,>]]*\.[^\s[",><]]+$""".toRegex()
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            if (sharedText.matches(urlRegex)) {
                val fullUrl =
                    if (sharedText.startsWith("http://") || sharedText.startsWith("https://")) {
                        sharedText
                    } else {
                        "http://$sharedText"
                    }
                addViewModel.createShareWebPage(fullUrl)
            } else {
                addViewModel.addShareMemo(sharedText)
            }
        }
    }

    private fun handleFile(intent: Intent) {
        val imageUri: Uri?
        if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            imageUri = intent.getParcelableExtra(
                Intent.EXTRA_STREAM,
                Uri::class.java
            )
        } else {
            imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
        }
        if (imageUri != null) {
            addViewModel.processSelectedUris(this, listOf(imageUri))
        }
    }

    private fun extractTextAndUrl(input: String): Array<String> {
        val urlRegex =
            """(?:https?://)?(?:www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,6}\b(?:[-a-zA-Z0-9@:%_+.~#?&/=]*)""".toRegex()
        val match = urlRegex.find(input)
        return if (match != null) {
            var url = match.value
            // URL에 http:// 또는 https://가 없으면 http://를 추가
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://$url"
            }
            val text = input.replace(match.value, "").trim()
            arrayOf(text, url, input)
        } else {
            arrayOf(input.trim(), "", input)
        }
    }

}

