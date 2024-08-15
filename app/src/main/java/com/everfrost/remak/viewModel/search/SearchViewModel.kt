package com.everfrost.remak.viewModel.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.repository.SearchHistoryRepository
import com.everfrost.remak.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchMode = MutableStateFlow(SearchType.NORMAL)
    val searchMode: MutableStateFlow<SearchType> = _searchMode

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: MutableStateFlow<List<String>> = _searchHistory

    private val _searchContent = MutableStateFlow(TextFieldValue())
    val searchContent: StateFlow<TextFieldValue> = _searchContent

    private val _isDataEnd = MutableStateFlow(false)
    val isDataEnd: StateFlow<Boolean> = _isDataEnd

    private val _searchList = MutableStateFlow<List<MainListModel.Data>>(emptyList())
    val searchList: StateFlow<List<MainListModel.Data>> = _searchList

    private val _searchListState = MutableStateFlow<APIResponse<MainListModel.Response>>(
        APIResponse.Empty()
    )
    val searchListState: StateFlow<APIResponse<MainListModel.Response>> = _searchListState

    private var offset: Int = 0


    fun getTextSearchResult(query: String) {
        if (isDataEnd.value) return
        _searchListState.value = APIResponse.Loading()
        viewModelScope.launch {
            _searchListState.value = searchRepository.getTextSearchData(query, offset)
            if (_searchListState.value is APIResponse.Success) {
                val data = (_searchListState.value as APIResponse.Success).data!!.data
                val tmpList = _searchList.value.toMutableList()
                data.forEach {
                    tmpList.add(it)
                }
                _searchList.value = tmpList
                offset += 20
            }
        }
    }

    fun getEmbeddingSearchResult(query: String) {
        if (isDataEnd.value) return
        _searchListState.value = APIResponse.Loading()
        viewModelScope.launch {
            _searchListState.value = searchRepository.getEmbeddingSearchData(query)
            if (_searchListState.value is APIResponse.Success) {
                val data = (_searchListState.value as APIResponse.Success).data!!.data
                val tmpList = _searchList.value.toMutableList()
                data.forEach {
                    tmpList.add(it)
                }
                _searchList.value = tmpList
            }
        }
    }


    fun resetSearchResult() {
        _searchList.value = emptyList()
        _searchListState.value = APIResponse.Empty()
        offset = 0
    }

    fun setSearchContent(content: TextFieldValue) {
        _searchContent.value = content
    }

    fun saveSearchHistory(query: String) = viewModelScope.launch {
        searchHistoryRepository.saveSearchHistory(query)
        getSearchHistory()
    }

    fun getSearchHistory() = viewModelScope.launch {
        _searchHistory.value = searchHistoryRepository.fetchSearchHistory()
    }

    fun deleteSearchHistory(query: String) = viewModelScope.launch {
        searchHistoryRepository.deleteSearchQuery(query)
        getSearchHistory()
    }

    fun changeSearchType(type: SearchType) {
        _searchMode.value = type
    }

}

enum class SearchType {
    NORMAL, EMBEDDED, TEXT
}