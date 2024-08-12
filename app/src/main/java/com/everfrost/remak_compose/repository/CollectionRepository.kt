package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.collection.CollectionListModel
import com.everfrost.remak_compose.model.home.main.MainListModel

interface CollectionRepository {
    //collection--------------------------------------------------------------------------------------------
    //컬렉션 리스트
    suspend fun getCollectionList(): APIResponse<CollectionListModel.Response>

}

class CollectionRepositoryImpl(
    private val remoteDataSource: RemoteDataSource

) : CollectionRepository {
    override suspend fun getCollectionList(): APIResponse<CollectionListModel.Response> {
        try {
            val response = remoteDataSource.getCollectionList()
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