package com.everfrost.remak_compose.viewModel.home.main

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel
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
    private val mainRepository: MainRepository
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

    private var currentDateType: String? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing


    fun fetchMainList() {
        _mainListState.value = APIResponse.Loading()
        viewModelScope.launch {
            val response = mainRepository.getMainList(cursor, docID)
            if (response is APIResponse.Success) {
                cursor = response.data!!.data.last().createdAt
                docID = response.data.data.last().docId
                _mainListState.value = response

                val newList = mutableListOf<MainListModel.Data>()

                for (data in response.data.data) {
                    data.updatedAt =
                        convertToUserTimezone(data.updatedAt!!, TimeZone.getDefault().id)
                    val dateType = classifyDate(data.updatedAt!!.toString())
                    if (dateType != currentDateType) {
                        currentDateType = dateType
                        newList.add(
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
                    newList.add(data)
                }
                _mainList.value = newList


            } else {
                _mainListState.value = APIResponse.Error(
                    message = response.message ?: "",
                    data = _mainListState.value.data,
                    errorCode = response.errorCode ?: "500",
                )
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

    fun testRefresh() {
        _isRefreshing.value = true

        viewModelScope.launch {
            delay(2000)
            _isRefreshing.value = false
        }

    }


}