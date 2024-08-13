package com.everfrost.remak_compose.viewModel.home.main

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.DeleteModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.repository.DocumentRepository
import com.everfrost.remak_compose.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class HomeMainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {
    private var lastScrollIndex = 0
    private val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean>
        get() = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    } // 상단 앱바 숨김 처리

    private val _mainListState =
        MutableStateFlow<APIResponse<MainListModel.Response>>(APIResponse.Empty())
    val mainListState: StateFlow<APIResponse<MainListModel.Response>> = _mainListState

    private val _mainList = MutableStateFlow<List<MainListModel.Data>>(emptyList())
    val mainList: StateFlow<List<MainListModel.Data>> = _mainList

    private var cursor: String? = null
    private var docID: String? = null
    private val _isDataEnd = MutableStateFlow(false)
    val isDataEnd: StateFlow<Boolean> = _isDataEnd

    private var currentDateType: String? = null

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _isInit = MutableStateFlow(false)
    val isInit: StateFlow<Boolean> = _isInit

    private val _deleteState =
        MutableStateFlow<APIResponse<DeleteModel.ResponseBody>>(APIResponse.Empty())
    val deleteState: StateFlow<APIResponse<DeleteModel.ResponseBody>> = _deleteState

    private val _deleteDialog = MutableStateFlow(false)
    val deleteDialog: StateFlow<Boolean> = _deleteDialog

    fun fetchMainList() {
        _isInit.value = true
        if (isDataEnd.value) return
        _mainListState.value = APIResponse.Loading()
        viewModelScope.launch {
            val response = mainRepository.getMainList(cursor, docID)
            if (response is APIResponse.Success) {
                _mainListState.value = response

                if (response.data!!.data.isEmpty()) {
                    _isDataEnd.value = true
                    return@launch
                }
                cursor = response.data!!.data.last().createdAt
                docID = response.data.data.last().docId

                val tmpData = _mainList.value.toMutableList()

                for (data in response.data.data) {
                    data.updatedAt =
                        convertToUserTimezone(data.updatedAt!!, TimeZone.getDefault().id)
                    val dateType = classifyDate(data.updatedAt!!.toString())
                    if (dateType != currentDateType) {
                        currentDateType = dateType
                        tmpData.add(
                            MainListModel.Data(
                                docId = null,
                                title = null,
                                type = "DATE",
                                url = null,
                                content = null,
                                summary = null,
                                status = null,
                                thumbnailUrl = null,
                                createdAt = null,
                                updatedAt = null,
                                tags = listOf(),
                                isSelected = false,
                                header = dateType
                            )
                        )
                    }
                    tmpData.add(data)
                }
                _mainList.value = tmpData
                Log.d("메인", "fetchMainList: ${_mainList.value}")


            } else {
                _mainListState.value = APIResponse.Error(
                    message = response.message ?: "",
                    data = _mainListState.value.data,
                    errorCode = response.errorCode ?: "500",
                )
                Log.d("메인", "fetchMainList: ${response.message}")
            }
        }
    }

    private fun classifyDate(dateString: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = ZonedDateTime.parse(dateString, formatter).toLocalDate()
        val today = LocalDate.now()
        return when {
            //오늘
            dateTime.isEqual(today) -> {
                "오늘"
            }
            //오늘 제외 7일 전
            dateTime.isAfter(today.minusDays(7)) -> {
                "최근 일주일"
            }
            //오늘 기준 30일 전
            dateTime.isAfter(today.minusDays(30)) -> {
                "최근 한달"
            }

            else -> {
                "그 이전"
            }
        }
    }

    private fun convertToUserTimezone(dateString: String, userTimezone: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
        val userZoneId = ZoneId.of(userTimezone)

        // Convert the datetime to user's timezone
        val userZonedDateTime = zonedDateTime.withZoneSameInstant(userZoneId)
        return userZonedDateTime.format(formatter)
    }

    fun resetMainList() {
        cursor = null
        docID = null
        _mainList.value = emptyList()
        _mainListState.value = APIResponse.Empty()
        _isDataEnd.value = false
        _isInit.value = false
    }

    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value
        _mainList.value.forEach {
            it.isSelected = false
        }
    }

    fun toggleSelect(index: Int) {
        _mainList.value = _mainList.value.toMutableList().also {
            it[index] = it[index].copy(isSelected = !it[index].isSelected)
        }
        if (_mainList.value.all { !it.isSelected }) {
            _isEditMode.value = false
        }
    }

    fun deleteDocument() {
        viewModelScope.launch {
            val selectedList = _mainList.value.filter { it.isSelected }
            if (selectedList.isEmpty()) return@launch
            for (data in selectedList) {
                val response = documentRepository.deleteDocument(data.docId!!)
                if (response is APIResponse.Success) {
                    _deleteState.value = response
                } else {
                    _deleteState.value = APIResponse.Error(
                        message = response.message ?: "",
                        data = response.data,
                        errorCode = response.errorCode ?: "500"
                    )
                }
            }

        }
    }

    fun getSelectedDocument(): List<String> {
        return _mainList.value.filter { it.isSelected }.map { it.docId!! }
    }

    fun setDeleteDialog(isShow: Boolean) {
        _deleteDialog.value = isShow
    }


}