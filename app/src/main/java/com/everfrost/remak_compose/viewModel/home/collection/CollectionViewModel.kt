package com.everfrost.remak_compose.viewModel.home.collection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.collection.AddDataInCollectionModel
import com.everfrost.remak_compose.model.collection.CollectionListModel
import com.everfrost.remak_compose.model.collection.CreateCollectionModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _collectionListState = MutableStateFlow<APIResponse<CollectionListModel.Response>>(
        APIResponse.Empty()
    )
    val collectionListState: StateFlow<APIResponse<CollectionListModel.Response>> =
        _collectionListState

    private val _collectionList = MutableStateFlow<List<CollectionListModel.Data>>(emptyList())
    val collectionList: StateFlow<List<CollectionListModel.Data>> = _collectionList

    private val _createCollectionState =
        MutableStateFlow<APIResponse<CreateCollectionModel.ResponseBody>>(
            APIResponse.Empty()
        )
    val createCollectionState: StateFlow<APIResponse<CreateCollectionModel.ResponseBody>> =
        _createCollectionState

    private val _collectionDetailListState = MutableStateFlow<APIResponse<MainListModel.Response>>(
        APIResponse.Empty()
    )
    val collectionDetailListState: StateFlow<APIResponse<MainListModel.Response>> =
        _collectionDetailListState

    private val _collectionDetailList = MutableStateFlow<List<MainListModel.Data>>(emptyList())
    val collectionDetailList: StateFlow<List<MainListModel.Data>> = _collectionDetailList

    private val _updateCollectionState =
        MutableStateFlow<APIResponse<AddDataInCollectionModel.RemoveResponse>>(
            APIResponse.Empty()
        )
    val updateCollectionState: StateFlow<APIResponse<AddDataInCollectionModel.RemoveResponse>> =
        _updateCollectionState

    private val _addDataInCollectionState =
        MutableStateFlow<APIResponse<AddDataInCollectionModel.AddResponse>>(
            APIResponse.Empty()
        )
    val addDataInCollectionState: StateFlow<APIResponse<AddDataInCollectionModel.AddResponse>> =
        _addDataInCollectionState

    private val _collectionName = MutableStateFlow("")
    val collectionName: StateFlow<String> = _collectionName
    private val _collectionDescription = MutableStateFlow("")
    val collectionDescription: StateFlow<String> = _collectionDescription
    private val _isActionComplete = MutableStateFlow<Boolean?>(null)
    val isActionComplete: StateFlow<Boolean?> = _isActionComplete
    private var cursor: String? = null
    private var docId: String? = null
    private val _isDataEnd = MutableStateFlow(false)
    val isDataEnd: StateFlow<Boolean> = _isDataEnd
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode
    private val _selectCount = MutableStateFlow(0)
    val selectCount: StateFlow<Int> = _selectCount

    private val _newName = MutableStateFlow("")
    val newName: StateFlow<String> = _newName

    private val _isAnyChecked = MutableStateFlow(false)
    val isAnyChecked: StateFlow<Boolean> = _isAnyChecked

    fun setNewName(value: String) {
        _newName.value = value
    }


    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun setCollectionName(value: String) {
        _collectionName.value = value
    }

    fun setCollectionDescription(value: String) {
        _collectionDescription.value = value
    }

    fun fetchCollectionList() {
        viewModelScope.launch {
            _collectionListState.value = collectionRepository.getCollectionList()
            if (_collectionListState.value is APIResponse.Success) {
                _collectionList.value = _collectionListState.value.data!!.data
            }
        }
    }

    fun resetActionComplete() {
        _isActionComplete.value = null
    }

    fun fetchCollectionDetailList(collectionName: String) {
        if (isDataEnd.value) return
        _collectionDetailListState.value = APIResponse.Loading()
        viewModelScope.launch {
            _collectionDetailListState.value =
                collectionRepository.getCollectionDetailList(collectionName, cursor, docId)
            if (_collectionDetailListState.value is APIResponse.Success) {
                if (_collectionDetailListState.value.data!!.data.isEmpty()) {
                    _isDataEnd.value = true
                    return@launch
                }
                val tmpData = _collectionDetailList.value.toMutableList()
                tmpData.addAll(_collectionDetailListState.value.data!!.data)
                _collectionDetailList.value = tmpData
                cursor = _collectionDetailListState.value.data!!.data.last().updatedAt
                docId = _collectionDetailListState.value.data!!.data.last().docId
            }
        }
    }

    fun createCollection() {
        viewModelScope.launch {
            _createCollectionState.value = collectionRepository.createCollection(
                _collectionName.value,
                _collectionDescription.value
            )
            if (_createCollectionState.value is APIResponse.Success) {
                _isActionComplete.value = true
            } else {
                _isActionComplete.value = false
            }
        }
    }

    fun updateCollection() {
        viewModelScope.launch {
            _updateCollectionState.value = collectionRepository.updateCollection(
                _collectionName.value,
                _newName.value,
                _collectionDescription.value
            )
            if (_updateCollectionState.value is APIResponse.Success) {
                _isActionComplete.value = true
            } else {
                _isActionComplete.value = false

            }
        }
    }


    fun resetCollectionDetailList() {
        _collectionDetailList.value = emptyList()
        cursor = null
        docId = null
    }

    fun toggleSelect(index: Int) {
        _collectionDetailList.value = _collectionDetailList.value.toMutableList().also {
            it[index] = it[index].copy(isSelected = !it[index].isSelected)
        }
        if (_collectionDetailList.value[index].isSelected) {
            increaseSelectCount()
        } else {
            decreaseSelectCount()
        }

    }

    fun resetSelect() {
        _collectionDetailList.value = _collectionDetailList.value.toMutableList().also {
            it.forEachIndexed { index, data ->
                it[index] = data.copy(isSelected = false)
            }
        }
        _selectCount.value = 0

    }

    private fun increaseSelectCount() {
        _selectCount.value += 1
    }

    private fun decreaseSelectCount() {
        _selectCount.value -= 1
    }

    fun isAnyChecked() {
        _isAnyChecked.value = _collectionList.value.any { it.isSelected }
    }

    fun toggleCollectionListCheck(index: Int) {
        _collectionList.value = _collectionList.value.toMutableList().also {
            it[index] = it[index].copy(isSelected = !it[index].isSelected)
        }
        isAnyChecked()
    }

    fun addDataInCollection(docIds: List<String>) {
        val collectionName = getSelectedCollections()
        viewModelScope.launch {
            for (item in collectionName) {
                _addDataInCollectionState.value =
                    collectionRepository.addDataInCollection(item, docIds)

            }
            _isActionComplete.value = true


        }
    }

    private fun getSelectedCollections(): List<String> {
        return _collectionList.value.filter { it.isSelected }.map { it.name }
    }


}