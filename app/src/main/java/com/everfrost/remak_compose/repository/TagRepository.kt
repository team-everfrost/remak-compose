package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.model.tag.TagListModel
import retrofit2.http.Query

interface TagRepository {
    suspend fun getTagList(
        offset: Int?,
        query: String?
    ): APIResponse<TagListModel.Response>
}

class TagRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : TagRepository {
    override suspend fun getTagList(
        offset: Int?,
        query: String?
    ): APIResponse<TagListModel.Response> {
        try {
            val response = remoteDataSource.getTagList(offset, query)
            return if (response.isSuccessful) {
                APIResponse.Success(data = response.body())
            } else {
                APIResponse.Error(
                    message = "message: ${
                        response.errorBody()!!.string()
                    }",
                    errorCode = response.code().toString()
                )
            }
        } catch (e: Exception) {
            return APIResponse.Error(
                message = "Sign in failed: ${e.message}",
                errorCode = "500"
            )
        }
    }
}