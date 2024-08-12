package com.everfrost.remak_compose.viewModel.home.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.model.tag.TagListModel
import com.everfrost.remak_compose.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _tagListState = MutableStateFlow<APIResponse<TagListModel.Response>>(
        APIResponse.Empty()
    )
    val tagListState: StateFlow<APIResponse<TagListModel.Response>> = _tagListState

    private val _tagDetailListState = MutableStateFlow<APIResponse<MainListModel.Response>>(
        APIResponse.Empty()
    )
    val tagDetailListState: StateFlow<APIResponse<MainListModel.Response>> = _tagDetailListState

    private val _tagDetailList = MutableStateFlow<List<MainListModel.Data>>(emptyList())
    val tagDetailList: StateFlow<List<MainListModel.Data>> = _tagDetailList

    private val _tagList = MutableStateFlow<List<TagListModel.Data>>(emptyList())
    val tagList: StateFlow<List<TagListModel.Data>> = _tagList
    private val _isDataEnd = MutableStateFlow(false)
    val isDataEnd: StateFlow<Boolean> = _isDataEnd
    private val _isInit = MutableStateFlow(false)
    val isInit: StateFlow<Boolean> = _isInit

    private var offset: Int = 0

    private var cursor: String? = null
    private var docId: String? = null


    fun setLinkText(text: String) {
        _searchText.value = text
    }

    fun fetchTagList() {
        _isInit.value = true
        if (isDataEnd.value) return
        _tagListState.value = APIResponse.Loading()
        viewModelScope.launch {
            _tagListState.value =
                tagRepository.getTagList(offset = offset, query = _searchText.value)
            if (_tagListState.value is APIResponse.Success) {
                if (_tagListState.value.data!!.data.isEmpty()) {
                    _isDataEnd.value = true
                    return@launch
                }
                val data = (_tagListState.value as APIResponse.Success).data!!.data
                val tmpTagList = _tagList.value.toMutableList()
                data.forEach {
                    tmpTagList.add(it)
                }
                _tagList.value = tmpTagList
                offset = offset!! + 20
            }
        }
    }

    fun resetTagList() {
        _tagList.value = emptyList()
        offset = 0
        _isDataEnd.value = false
    }

    fun fetchTagDetailList(tagName: String) {
        _isInit.value = true
        if (isDataEnd.value) return
        _tagDetailListState.value = APIResponse.Loading()
        viewModelScope.launch {
            _tagDetailListState.value =
                tagRepository.getTagDetailData(tagName, cursor = null, docId = null)
            if (_tagDetailListState.value is APIResponse.Success) {
                if ((_tagDetailListState.value as APIResponse.Success).data!!.data.isEmpty()) {
                    _isDataEnd.value = true
                    return@launch
                }
                val data = (_tagDetailListState.value as APIResponse.Success).data!!.data
                val tmpTagDetailList = _tagDetailList.value.toMutableList()
                data.forEach {
                    tmpTagDetailList.add(it)
                }
                _tagDetailList.value = tmpTagDetailList
                cursor = data.last().updatedAt
                docId = data.last().docId
            }
        }
    }
}