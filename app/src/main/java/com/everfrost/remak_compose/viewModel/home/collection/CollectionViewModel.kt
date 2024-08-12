package com.everfrost.remak_compose.viewModel.home.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.collection.CollectionListModel
import com.everfrost.remak_compose.model.collection.CreateCollectionModel
import com.everfrost.remak_compose.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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


    private val _collectionName = MutableStateFlow("")
    val collectionName: StateFlow<String> = _collectionName
    private val _collectionDescription = MutableStateFlow("")
    val collectionDescription: StateFlow<String> = _collectionDescription
    private val _isActionComplete = MutableStateFlow<Boolean?>(null)
    val isActionComplete: StateFlow<Boolean?> = _isActionComplete

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

}