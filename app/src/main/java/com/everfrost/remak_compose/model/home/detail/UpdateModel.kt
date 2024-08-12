package com.everfrost.remak_compose.model.home.detail

class UpdateModel {

    data class MemoRequestBody(
        val content: String,
    )

    data class MemoResponseBody(
        val message: String,
        val data: MemoData
    )

    data class MemoData(
        val docId: String,
        val title: String?,
        val type: String,
        val url: String?,
        val content: String,
        val summary: String?,
        val status: String,
        val thumbnailUrl: String?,
        val createdAt: String,
        val updatedAt: String,
    )

}