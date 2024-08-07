package com.everfrost.remak_compose.model.home.file

class UploadFileModel {

    data class ResponseBody(
        val message: String,
        val data: List<Data>
    )

    data class Data(
        val docId: String,
        val title: String?,
        val type: String,
        val url: String?,
        val content: String?,
        val summary: String?,
        val status: String,
        val thumbnailUrl: String?,
        val createdAt: String,
        val updatedAt: String,
        val tags: List<String>
    )

}