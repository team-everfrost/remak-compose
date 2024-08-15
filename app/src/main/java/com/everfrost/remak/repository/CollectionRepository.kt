package com.everfrost.remak.repository

import com.everfrost.remak.dataSource.RemoteDataSource
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.DeleteModel
import com.everfrost.remak.model.collection.AddDataInCollectionModel
import com.everfrost.remak.model.collection.CollectionListModel
import com.everfrost.remak.model.collection.CreateCollectionModel
import com.everfrost.remak.model.home.main.MainListModel

interface CollectionRepository {
    //collection--------------------------------------------------------------------------------------------
    //컬렉션 리스트
    suspend fun getCollectionList(): APIResponse<CollectionListModel.Response>

    //컬렉션 추가
    suspend fun createCollection(
        name: String,
        description: String
    ): APIResponse<CreateCollectionModel.ResponseBody>

    //컬렉션 디테일 리스트
    suspend fun getCollectionDetailList(
        collectionId: String,
        cursor: String?,
        docId: String?
    ): APIResponse<MainListModel.Response>

    //컬렉션 수정
    suspend fun updateCollection(
        name: String,
        newName: String,
        description: String?
    ): APIResponse<AddDataInCollectionModel.RemoveResponse>

    //컬렉션에 자료추가
    suspend fun addDataInCollection(
        name: String,
        docIds: List<String>
    ): APIResponse<AddDataInCollectionModel.AddResponse>

    //컬렉션에서 자료 삭제
    suspend fun removeDataInCollection(
        name: String,
        docIds: List<String>
    ): APIResponse<AddDataInCollectionModel.RemoveResponse>

    //컬렉션 삭제
    suspend fun deleteCollection(name: String): APIResponse<DeleteModel.ResponseBody>
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

    override suspend fun createCollection(
        name: String,
        description: String
    ): APIResponse<CreateCollectionModel.ResponseBody> {
        try {
            val response = remoteDataSource.createCollection(name, description)
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

    override suspend fun getCollectionDetailList(
        collectionId: String,
        cursor: String?,
        docId: String?
    ): APIResponse<MainListModel.Response> {
        try {
            val response = remoteDataSource.getCollectionDetailList(collectionId, cursor, docId)
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

    override suspend fun updateCollection(
        name: String,
        newName: String,
        description: String?
    ): APIResponse<AddDataInCollectionModel.RemoveResponse> {
        try {
            val response = remoteDataSource.updateCollection(name, newName, description)
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

    override suspend fun addDataInCollection(
        name: String,
        docIds: List<String>
    ): APIResponse<AddDataInCollectionModel.AddResponse> {
        try {
            val response = remoteDataSource.addDataInCollection(name, docIds)
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

    override suspend fun removeDataInCollection(
        name: String,
        docIds: List<String>
    ): APIResponse<AddDataInCollectionModel.RemoveResponse> {
        try {
            val response = remoteDataSource.removeDataInCollection(name, docIds)
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

    override suspend fun deleteCollection(name: String): APIResponse<DeleteModel.ResponseBody> {
        try {
            val response = remoteDataSource.deleteCollection(name)
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