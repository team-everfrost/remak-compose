package com.everfrost.remak.repository

import com.everfrost.remak.dataSource.RemoteDataSource
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.model.tag.TagListModel

interface TagRepository {
    suspend fun getTagList(
        offset: Int?,
        query: String?
    ): APIResponse<TagListModel.Response>

    suspend fun getTagDetailData(
        tagName: String,
        cursor: String?,
        docId: String?
    ): APIResponse<MainListModel.Response>
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

    override suspend fun getTagDetailData(
        tagName: String,
        cursor: String?,
        docId: String?
    ): APIResponse<MainListModel.Response> {
        try {
            val response = remoteDataSource.getTagDetailData(tagName, cursor, docId)
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