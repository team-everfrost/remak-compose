import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.search.SearchEmbeddedModeSection
import com.everfrost.remak_compose.view.search.SearchNormalModeSection
import com.everfrost.remak_compose.view.search.SearchTextModeSection
import com.everfrost.remak_compose.viewModel.search.SearchType
import com.everfrost.remak_compose.viewModel.search.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {
    var debounceJob: Job? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val searchContent by viewModel.searchContent.collectAsState()
    val searchMode by viewModel.searchMode.collectAsState()
    val searchList by viewModel.searchList.collectAsState()
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(true) {
        viewModel.getSearchHistory()
        focusRequester.requestFocus()
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
                Text(
                    text = "검색", style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendard
                    )
                )
                TagSearchTextField(
                    onEnter = {
                        viewModel.saveSearchHistory(searchContent.text)
                        viewModel.resetSearchResult()
                        viewModel.changeSearchType(SearchType.EMBEDDED)
                        viewModel.getEmbeddingSearchResult(searchContent.text)
                    },
                    value = searchContent,
                    onValueChange = {
                        viewModel.setSearchContent(it)
                        debounceJob?.cancel()
                        debounceJob = coroutineScope.launch {
                            delay(400) // 300ms 딜레이
                            viewModel.resetSearchResult()
                            if (it.text.isEmpty()) {
                                viewModel.changeSearchType(SearchType.NORMAL)
                            } else {
                                viewModel.getTextSearchResult(it.text)
                                viewModel.changeSearchType(SearchType.TEXT)

                            }
                        }
                    },
                    placeholder = "검색어를 입력해보세요",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .focusRequester(focusRequester) // 포커
                )

                when (searchMode) {
                    SearchType.NORMAL -> {
                        SearchNormalModeSection(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .fillMaxWidth(),
                            searchHistory = searchHistory,
                            keywordClick = {
                                val updatedContent = TextFieldValue(
                                    text = it,
                                    selection = TextRange(it.length)
                                )
                                viewModel.setSearchContent(updatedContent)
                                viewModel.saveSearchHistory(it)
                                viewModel.resetSearchResult()
                                viewModel.changeSearchType(SearchType.EMBEDDED)
                                viewModel.getEmbeddingSearchResult(it)
                            },
                            deleteClick = {
                                viewModel.deleteSearchHistory(it)
                            },
                        )
                    }

                    SearchType.TEXT -> {
                        SearchTextModeSection(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .fillMaxWidth(),
                            searchList = searchList,
                            navController = navController,
                            viewModel = viewModel
                        )
                    }

                    else -> {
                        SearchEmbeddedModeSection(
                            navController = navController,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp)
                        )


                    }
                }
            }
        }

    }
}