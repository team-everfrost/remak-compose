package com.everfrost.remak_compose.viewModel.home.detail.link

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LinkDetailViewModel @Inject constructor(
    private val documentRepository: DocumentRepository

) : ViewModel() {
    private val _getDetailDataState = MutableStateFlow<APIResponse<MainListModel.DetailResponse>>(
        APIResponse.Empty()
    )
    val getDetailDataState: StateFlow<APIResponse<MainListModel.DetailResponse>> =
        _getDetailDataState

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title
    private val _tagList = MutableStateFlow<List<String>>(emptyList())
    val tagList: StateFlow<List<String>> = _tagList
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary
    private val _linkData = MutableStateFlow("")
    val linkData: StateFlow<String> = _linkData
    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded

    fun dataLoaded() {
        _isDataLoaded.value = true
    }

    fun fetchDetailData(docId: String) {
        viewModelScope.launch {
            _getDetailDataState.value = documentRepository.getDetailData(docId)

            if (_getDetailDataState.value is APIResponse.Success) {
                Log.d("FileDetailViewModel", _getDetailDataState.value.data!!.data.toString())
                val data = (_getDetailDataState.value as APIResponse.Success).data!!.data
                val tmpTagList = mutableListOf<String>()
                data.tags.forEach {
                    tmpTagList.add(it.toString())
                }
                _date.value = formatDate(data.createdAt!!)
                _title.value = data.title!!
                _tagList.value = tmpTagList
                _summary.value = formatSummary(data.summary ?: "")
                _url.value = data.url!!
                _linkData.value = prepareLinkData(data.content!!)
            } else {
                Log.d("FileDetailViewModel", _getDetailDataState.value.message.toString())
            }
        }
    }

    private fun prepareLinkData(data: String): String {
        return data
            .replace(Regex("\\\\t"), "    ")
            .replace(Regex("\\\\n"), "<br>")
            .replace(Regex("\\\\r"), "<br>")
            .replace(Regex("#"), "%23")
            .replace(Regex("</p>"), "</p><br>")
    }

    private fun formatDate(date: String): String { //2024-08-07T07:09:04.180Z -> 2024.08.07
        val zonedDateTime = ZonedDateTime.parse(date)
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return zonedDateTime.format(formatter)
    }

    private fun formatSummary(summary: String): String { //첫줄 제거
        val lines = summary.split("\n")
        if (lines.size > 1) {
            return lines.subList(1, lines.size).joinToString("\n")
        } else {
            return summary
        }
    }

}