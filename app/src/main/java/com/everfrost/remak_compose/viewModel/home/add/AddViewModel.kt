package com.everfrost.remak_compose.viewModel.home.add

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
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

    private val _isActionComplete = mutableStateOf(false)
    val isActionComplete: State<Boolean> = _isActionComplete

    private val _isFileTooLarge = mutableStateOf(false)
    val isFileTooLarge: State<Boolean> = _isFileTooLarge

    private val _uploadFileResponse =
        MutableStateFlow<APIResponse<UploadFileModel.ResponseBody>>(APIResponse.Empty())
    val uploadFileResponse: StateFlow<APIResponse<UploadFileModel.ResponseBody>> =
        _uploadFileResponse


    private fun uploadFile(fileList: List<MultipartBody.Part>) {
        _uploadState.value = UploadState.LOADING
        viewModelScope.launch {
            _uploadFileResponse.value = documentRepository.uploadFile(fileList)
            if (_uploadFileResponse.value is APIResponse.Success) {
                _uploadState.value = UploadState.SUCCESS
            } else {
                _uploadState.value = UploadState.FAIL
                Log.d(
                    "AddViewModel",
                    "uploadFile: ${(_uploadFileResponse.value as APIResponse.Error).message}"
                )
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

