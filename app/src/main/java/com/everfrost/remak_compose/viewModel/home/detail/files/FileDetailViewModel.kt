package com.everfrost.remak_compose.viewModel.home.detail.files

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FileDetailViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _getDetailDataState = MutableStateFlow<APIResponse<MainListModel.DetailResponse>>(
        APIResponse.Empty()
    )
    val getDetailDataState: StateFlow<APIResponse<MainListModel.DetailResponse>> =
        _getDetailDataState

    private val _downloadFileState = MutableStateFlow<APIResponse<DownloadModel.ResponseBody>>(
        APIResponse.Empty()
    )

    private val _shareFileState = MutableStateFlow<APIResponse<DownloadModel.ResponseBody>>(
        APIResponse.Empty()
    )

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title
    private val _tagList = MutableStateFlow<List<String>>(emptyList())
    val tagList: StateFlow<List<String>> = _tagList
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary
    private val _isFileShareEnabled = MutableStateFlow(false)
    val isFileShareEnabled: StateFlow<Boolean> = _isFileShareEnabled

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

    fun downloadFile(context: Context) {
        viewModelScope.launch {
            _downloadFileState.value =
                documentRepository.downloadFile(_getDetailDataState.value.data!!.data.docId!!)
            if (_downloadFileState.value is APIResponse.Success) {
                val url = _downloadFileState.value.data!!.data
                val request = DownloadManager.Request(Uri.parse(url))
                    .setTitle(title.value)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        title.value
                    )
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                withContext(Dispatchers.Main) {
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)
                    Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("FileDetailViewModel", _downloadFileState.value.message.toString())
            }
        }
    }

    fun shareFile(context: Context) = viewModelScope.launch {
        _isFileShareEnabled.value = false
        _shareFileState.value =
            documentRepository.downloadFile(_getDetailDataState.value.data!!.data.docId!!)
        if (_shareFileState.value is APIResponse.Success) {
            val url = _shareFileState.value.data!!.data
            downloadAndShareFile(context, url!!, title.value)
        }


    }

    private suspend fun downloadAndShareFile(
        context: Context,
        fileUrl: String,
        fileName: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()
                val inputStream = connection.getInputStream()

                val uri = saveImageToInternalStorage(context, fileName, inputStream)
                withContext(Dispatchers.Main) {
                    _isFileShareEnabled.value = true
                    shareFileUri(context, uri, fileName)
                }
            } catch (e: Exception) {
                Log.d("FileDetailViewModel", e.toString())
            }

        }
    }

    private fun shareFileUri(context: Context, uri: Uri, fileName: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val resInfoList = context.packageManager.queryIntentActivities(shareIntent, 0)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, fileName)
        )
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


}