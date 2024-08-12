package com.everfrost.remak_compose.viewModel.home.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.collection.CollectionListModel
import com.everfrost.remak_compose.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _collectionListState = MutableStateFlow<APIResponse<CollectionListModel.Response>>(
        APIResponse.Empty()
    )
    val collectionListState = _collectionListState

    private val _collectionList = MutableStateFlow<List<CollectionListModel.Data>>(emptyList())
    val collectionList = _collectionList

    fun fetchCollectionList() {
        viewModelScope.launch {
            _collectionListState.value = collectionRepository.getCollectionList()
            if (_collectionListState.value is APIResponse.Success) {
                _collectionList.value = _collectionListState.value.data!!.data
            }
        }
    }

}