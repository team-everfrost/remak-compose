package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.home.main.MainListModel


interface MainRepository {
    suspend fun getMainList(cursor: String?, docID: String?): APIResponse<MainListModel.Response>
}

class MainRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : MainRepository {
    override suspend fun getMainList(
        cursor: String?,
        docID: String?
    ): APIResponse<MainListModel.Response> {
        try {
            val response = remoteDataSource.getMainList(cursor, docID)
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