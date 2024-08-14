package com.everfrost.remak_compose.viewModel.home.add

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.add.CreateModel
import com.everfrost.remak_compose.model.home.file.UploadFileModel
import com.everfrost.remak_compose.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {
    private val _uploadState = MutableStateFlow(UploadState.IDLE)
    val uploadState: StateFlow<UploadState> = _uploadState

    private val _isActionComplete = MutableStateFlow(false)
    val isActionComplete: StateFlow<Boolean> = _isActionComplete

    private val _isFileTooLarge = MutableStateFlow(false)
    val isFileTooLarge: StateFlow<Boolean> = _isFileTooLarge

    private val _uploadFileResponse =
        MutableStateFlow<APIResponse<UploadFileModel.ResponseBody>>(APIResponse.Empty())
    val uploadFileResponse: StateFlow<APIResponse<UploadFileModel.ResponseBody>> =
        _uploadFileResponse

    private val _linkText = MutableStateFlow("")
    val linkText: StateFlow<String> = _linkText

    private val _memoText = MutableStateFlow("")
    val memoText: StateFlow<String> = _memoText

    private val _createWebPageState =
        MutableStateFlow<APIResponse<CreateModel.WebPageResponseBody>>(APIResponse.Empty())
    val createWebPageState: StateFlow<APIResponse<CreateModel.WebPageResponseBody>> =
        _createWebPageState

    private val _addMemoState =
        MutableStateFlow<APIResponse<CreateModel.MemoResponseBody>>(APIResponse.Empty())
    val addMemoState: StateFlow<APIResponse<CreateModel.MemoResponseBody>> = _addMemoState

    fun setLinkText(value: String) {
        _linkText.value = value
    }

    fun setIsFileTooLarge(value: Boolean) {
        _isFileTooLarge.value = value
    }

    fun setIsActionComplete(value: Boolean) {
        _isActionComplete.value = value
    }

    fun setMemoText(value: String) {
        _memoText.value = value
    }

    fun addMemo() {
        viewModelScope.launch {
            _addMemoState.value =
                documentRepository.createMemo(CreateModel.MemoRequestBody(_memoText.value))
            if (_addMemoState.value is APIResponse.Success) {
                _isActionComplete.value = true
            }
        }
    }

    fun addShareMemo(memo: String) {
        viewModelScope.launch {
            _addMemoState.value =
                documentRepository.createMemo(CreateModel.MemoRequestBody(memo))
            if (_addMemoState.value is APIResponse.Success) {
                _isActionComplete.value = true
            }
        }
    }

    fun createShareWebPage(url: String) {
        viewModelScope.launch {
            _createWebPageState.value = documentRepository.createWebPage(url)
            if (_createWebPageState.value is APIResponse.Success) {
                _isActionComplete.value = true
            }
        }
    }

    fun createWebPage() {
        Log.d("AddViewModel", _linkText.value)
        val urlList = formatWebPageUrl()
        Log.d("AddViewModel", "createWebPage: $urlList")
        if (urlList.isEmpty()) {
            return
        }
        viewModelScope.launch {
            for (url in urlList) {
                _createWebPageState.value = documentRepository.createWebPage(url)
            }
            if (_createWebPageState.value is APIResponse.Success) {
                _isActionComplete.value = true
            }
        }
    }

    private fun formatWebPageUrl(): List<String> {
        val url = _linkText.value.trim()
        val splitText = url.split("\\n|\\s".toRegex()) //줄바꿈, 공백으로 구분
        val tmpList = mutableListOf<String>()
        val urlList = mutableListOf<String>()
        for (text in splitText) {
            tmpList.add(text)
        }
        for (i in tmpList) {
            var beforeUrl = i
            if (!i.startsWith("http://") && !i.startsWith("https://")) {
                beforeUrl = "https://$i"
                urlList.add(beforeUrl)
            } else {
                urlList.add(beforeUrl)
            }
        }
        return urlList
    }


    private fun uploadFile(fileList: List<MultipartBody.Part>) {
        _uploadState.value = UploadState.LOADING
        viewModelScope.launch {
            _uploadFileResponse.value = documentRepository.uploadFile(fileList)
            if (_uploadFileResponse.value is APIResponse.Success) {
                _uploadState.value = UploadState.SUCCESS
                _isActionComplete.value = true
            } else {
                _uploadState.value = UploadState.FAIL
                if (_uploadFileResponse.value.errorCode == "413") {
                    _isFileTooLarge.value = true
                }
            }
        }

    }

    fun processSelectedUris(context: Context, uris: List<Uri>) {
        if (uris.size > 10) {
            return
        }
        val fileList = mutableListOf<MultipartBody.Part>()
        for (uri in uris) {
            val mimeType = context.contentResolver.getType(uri)
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = inputStreamToFile(inputStream!!, uri, context)
            val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
            fileList.add(
                MultipartBody.Part.createFormData(
                    "files",
                    file.name,
                    requestFile
                )
            )
        }
        uploadFile(fileList)
    }

    private fun inputStreamToFile(inputStream: InputStream, uri: Uri, context: Context): File {
        val fileName = getFileNameFromUri(uri, context)
        val file = File(context.cacheDir, fileName!!)
        file.outputStream().use { fileOutputStream ->
            inputStream.copyTo(fileOutputStream)
        }
        return file
    }

    private fun getFileNameFromUri(uri: Uri, context: Context): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName

    }


}


enum class UploadState {
    LOADING, SUCCESS, FAIL, IDLE
}

