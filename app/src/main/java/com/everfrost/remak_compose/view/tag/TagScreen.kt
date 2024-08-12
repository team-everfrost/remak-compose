import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.ui.theme.bgGray2
import com.everfrost.remak_compose.ui.theme.pretendard
import com.everfrost.remak_compose.view.BottomNav
import com.everfrost.remak_compose.view.tag.TagListItem
import com.everfrost.remak_compose.viewModel.home.tag.TagViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun TagScreen(
    navController: NavController,
    viewModel: TagViewModel
) {
    val scrollState = rememberLazyListState()
    val tagText by viewModel.searchText.collectAsState()
    val tagList by viewModel.tagList.collectAsState()
    val tagListState by viewModel.tagListState.collectAsState()
    val isDataEnd by viewModel.isDataEnd.collectAsState()
    var debounceJob: Job? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.fetchTagList()
    }

    LaunchedEffect(scrollState, viewModel.isDataEnd) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .debounce(200L)
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= tagList.size - 1 && tagListState !is APIResponse.Loading && !isDataEnd) {
                    viewModel.fetchTagList()
                }
            }
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
                    text = "태그", style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendard
                    )
                )

                TagSearchTextField(
                    value = tagText,
                    onValueChange = {
                        viewModel.setLinkText(it)
                        debounceJob?.cancel()
                        debounceJob = coroutineScope.launch {
                            delay(300) // 300ms 딜레이
                            viewModel.resetTagList()
                            viewModel.fetchTagList()
                        }
                    },
                    placeholder = "태그를 검색해보세요",
                    keyboardOptions = KeyboardOptions(),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(top = 20.dp)
                ) {
                    items(tagList.size) { index ->
                        TagListItem(
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .fillMaxWidth()
                                .height(72.dp),
                            tagName = tagList[index].name,
                            tagCount = tagList[index].count
                        )
                    }

                }


            } // Column
        }

    }
}