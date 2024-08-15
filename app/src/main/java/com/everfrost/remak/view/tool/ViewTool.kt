package com.everfrost.remak.view.tool

object ViewTool {

    fun dateFormatting(date: String): String {
        return date.split("T")?.get(0) ?: ""

    }

    fun extractDomain(url: String): String? {
        val regex = """https?://([\w\-\.]+)""".toRegex()
        val result = regex.find(url!!)
        return result?.groups?.get(1)?.value
    }
}