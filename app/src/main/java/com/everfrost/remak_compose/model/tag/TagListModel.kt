package com.everfrost.remak_compose.model.tag

class TagListModel {

    data class Response(
        val message: String,
        var data: List<Data>
    )

    data class Data(
        val name: String,
        val count: Int,
    )
}