package com.everfrost.remak.viewModel.home.detail.link

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.repository.DocumentDatabaseRepository
import com.everfrost.remak.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LinkDetailViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val documentDatabaseRepository: DocumentDatabaseRepository

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

    private val _isDeleteComplete = MutableStateFlow(false)
    val isDeleteComplete: StateFlow<Boolean> = _isDeleteComplete

    private val _isImageShareReady = MutableStateFlow(false)

    private val _isSelfShareSuccess = MutableStateFlow(false)
    val isSelfShareSuccess: StateFlow<Boolean> = _isSelfShareSuccess

    fun dataLoaded() {
        _isDataLoaded.value = true
    }

//    fun fetchDetailData(docId: String) {
//        viewModelScope.launch {
//            _getDetailDataState.value = documentRepository.getDetailData(docId)
//            if (_getDetailDataState.value is APIResponse.Success) {
//                Log.d("FileDetailViewModel", _getDetailDataState.value.data!!.data.toString())
//                val data = (_getDetailDataState.value as APIResponse.Success).data!!.data
//                val tmpTagList = mutableListOf<String>()
//                data.tags.forEach {
//                    tmpTagList.add(it.toString())
//                }
//                _date.value = formatDate(data.createdAt!!)
//                _title.value = data.title!!
//                _tagList.value = tmpTagList
//                _summary.value = formatSummary(data.summary ?: "")
//                _url.value = data.url!!
//                _linkData.value = prepareLinkData(data.content!!)
//                if (data.status != "COMPLETED") {
//                    _isDataLoaded.value = true
//                }
//            } else {
//                Log.d("FileDetailViewModel", _getDetailDataState.value.message.toString())
//            }
//        }
//    }

    fun fetchDetailData(docId: String) {
        viewModelScope.launch {
            documentDatabaseRepository.getDetailData(docId).collect { response ->
                _getDetailDataState.value = response
                if (response is APIResponse.Success) {
                    val data = response.data!!.data
                    val tmpTagList = data.tags.map { it.toString() }
                    Log.d("tagsize", data.tags.size.toString());

                    _date.value = formatDate(data.createdAt!!)
                    _title.value = data.title!!
                    if (tmpTagList.isNotEmpty()) {
                        _tagList.value = tmpTagList
                    } else {
                        _tagList.value = emptyList()
                    }
                    _summary.value = formatSummary(data.summary ?: "")
                    _url.value = data.url!!
                    _linkData.value = prepareLinkData(data.content!!)
                    if (data.status != "COMPLETED") {
                        _isDataLoaded.value = true
                    }

                } else {
                    Log.d("FileDetailViewModel", _getDetailDataState.value.message.toString())

                }
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

    fun deleteDocument(docIdx: String) {
        viewModelScope.launch {
            val response = documentRepository.deleteDocument(docIdx)
            if (response is APIResponse.Success) {
                _isDeleteComplete.value = true
            }
        }
    }

    suspend fun shareSelf(
        context: Context,
        imageUrl: String,
    ) {
        val fileName = URLUtil.guessFileName(imageUrl, null, null)
        withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()
                val inputStream = connection.getInputStream()
                val uri = saveImageToInternalStorage(context, fileName, inputStream)
                withContext(Dispatchers.Main) {
                    uriToFile(context, uri)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri) {
        val fileList = mutableListOf<MultipartBody.Part>()
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
        uploadFile(fileList)
    }

    fun setIsSelfShareSuccess(value: Boolean) {
        _isSelfShareSuccess.value = value
    }

    /** 파일 업로드 */
    private fun uploadFile(files: List<MultipartBody.Part>) = viewModelScope.launch {
        _isSelfShareSuccess.value = false
        try {
            val response = documentRepository.uploadFile(files)
            if (response is APIResponse.Success) {
                _isSelfShareSuccess.value = true
            } else {
            }
        } catch (e: Exception) {
        }
    }

    private fun inputStreamToFile(inputStream: InputStream, uri: Uri, context: Context): File {
        val fileName = getFileNameFromUri(context, uri)
        val file = File(context.cacheDir, fileName!!)

        inputStream.use { input ->
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        return file
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            fileName = it.getString(nameIndex)
        }
        return fileName
    }

    fun downloadImage(url: String, context: Context) {
        Log.d("LinkDetailViewModel", "downloadImage: $url")
        val fileName = URLUtil.guessFileName(url, null, null)
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "Remak/$fileName"
        )
        val manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        Toast.makeText(context, "다운로드가 시작되었습니다.", Toast.LENGTH_SHORT).show()

    }

    suspend fun downloadAndShareImage(
        context: Context,
        imageUrl: String,
        fileName: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()
                val inputStream = connection.getInputStream()

                val uri = saveImageToInternalStorage(context, fileName, inputStream)
                withContext(Dispatchers.Main) {
                    _isImageShareReady.value = true
                    shareImageUri(context, uri, fileName)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun saveImageToInternalStorage(
        context: Context,
        fileName: String,
        inputStream: InputStream
    ): Uri {
        Log.d("File Name", fileName)
        val file = File(context.cacheDir, fileName)
        file.outputStream().use { fileOutput ->
            inputStream.use { input ->
                input.copyTo(fileOutput)
            }
        }
        Log.d("File Size", file.length().toString()) // 로그 추가
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun shareImageUri(context: Context, uri: Uri, fileName: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        // Explicitly grant URI permission to the target package
        val resInfoList = context.packageManager.queryIntentActivities(shareIntent, 0)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(shareIntent, "이미지 공유")
        )
    }

}