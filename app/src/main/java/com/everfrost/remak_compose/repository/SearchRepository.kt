package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel


interface SearchRepository {
    // 텍스트검색
    suspend fun getTextSearchData(
        query: String,
        offset: Int?
    ): APIResponse<MainListModel.Response>

    // 임베딩 검색
    suspend fun getEmbeddingSearchData(
        query: String,
    ): APIResponse<MainListModel.Response>
}

class SearchRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : SearchRepository {
    override suspend fun getTextSearchData(
        query: String,
        offset: Int?
    ): APIResponse<MainListModel.Response> {
        try {
            val response = remoteDataSource.getTextSearchData(query, offset)
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

    override suspend fun getEmbeddingSearchData(
        query: String,
    ): APIResponse<MainListModel.Response> {
        try {
            val response = remoteDataSource.getEmbeddingSearchData(query)
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