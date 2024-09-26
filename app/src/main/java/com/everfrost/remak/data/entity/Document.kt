package com.everfrost.remak.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey val docId: String,
    val date: String?,
    val title: String?,
    val tagList: List<String?>,
    val summary: String?,
    val linkData: String?,
    val url: String?,
)