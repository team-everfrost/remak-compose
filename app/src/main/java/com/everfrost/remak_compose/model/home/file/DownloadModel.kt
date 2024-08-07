package com.everfrost.remak_compose.model.home.file

class DownloadModel {
    data class ResponseBody(
        val message: String,
        val data: String?
    )
}