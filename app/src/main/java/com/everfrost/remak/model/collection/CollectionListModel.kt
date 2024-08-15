package com.everfrost.remak.model.collection

class CollectionListModel {
    data class Response(
        val message: String,
        var data: List<Data>
    )

    data class Data(
        val name: String,
        val description: String,
        val count: Int,
        var isSelected: Boolean = false
    )

}