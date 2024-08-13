package com.everfrost.remak_compose.viewModel.home.detail.memo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.detail.UpdateModel
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
class MemoDetailViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _getDetailDataState = MutableStateFlow<APIResponse<MainListModel.DetailResponse>>(
        APIResponse.Empty()
    )
    val getDetailDataState: StateFlow<APIResponse<MainListModel.DetailResponse>> =
        _getDetailDataState

    private val _updateMemoState = MutableStateFlow<APIResponse<UpdateModel.MemoResponseBody>>(
        APIResponse.Empty()
    )
    val updateMemoState: StateFlow<APIResponse<UpdateModel.MemoResponseBody>> = _updateMemoState

    private val _isDeleteComplete = MutableStateFlow(false)
    val isDeleteComplete: StateFlow<Boolean> = _isDeleteComplete
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode
    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date
    private val _initialMemo = MutableStateFlow("")
    val initialMemo: StateFlow<String> = _initialMemo
    private val _memo = MutableStateFlow("")
    val memo: StateFlow<String> = _memo
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary
    private val _editCancelDialog = MutableStateFlow(false)
    val editCancelDialog: StateFlow<Boolean> = _editCancelDialog

    fun toggleEditCancelDialog() {
        _editCancelDialog.value = !_editCancelDialog.value
    }

    fun changeEditMode(value: Boolean) {
        _isEditMode.value = value
    }

    fun setMemo(memo: String) {
        _memo.value = memo
    }

    private fun editComplete() {
        _isEditMode.value = false
    }

    fun updateMemo(
        docId: String,
    ) {
        viewModelScope.launch {
            _updateMemoState.value = documentRepository.updateMemo(
                docId = docId,
                body = UpdateModel.MemoRequestBody(
                    content = _memo.value
                )
            )
        }
        editComplete()
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
                _date.value = formatDate(data.updatedAt!!)
                _summary.value = formatSummary(data.summary ?: "")
                _initialMemo.value = data.content ?: ""
                _memo.value = data.content ?: ""
            } else {
                Log.d("FileDetailViewModel", _getDetailDataState.value.message.toString())
            }
        }
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

    fun deleteDocument(docId: String) {
        viewModelScope.launch {
            val response = documentRepository.deleteDocument(docId)
            if (response is APIResponse.Success) {
                _isDeleteComplete.value = true
            }
        }
    }


}