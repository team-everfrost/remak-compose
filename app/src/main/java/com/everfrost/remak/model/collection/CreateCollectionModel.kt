package com.everfrost.remak.model.collection

class CreateCollectionModel {

    data class RequestBody(
        val name: String,
        val description: String?,
    )

    data class ResponseBody(
        val message: String,
        val data: Data
    )

    data class Data(
        val name: String,
        val description: String?,
        val count: Int,
    )
}