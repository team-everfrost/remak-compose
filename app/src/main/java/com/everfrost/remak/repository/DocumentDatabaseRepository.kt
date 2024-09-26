package com.everfrost.remak.repository

import com.everfrost.remak.data.Dao.DocumentDao
import com.everfrost.remak.data.entity.Document
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DocumentDatabaseRepository @Inject constructor(
    private val documentDao: DocumentDao,
    private val api: ApiService
) {
    fun getAllDocuments() = documentDao.getAllDocuments()

    fun getDocumentById(docId: String) = documentDao.getDocument(docId)

    suspend fun insertDocument(document: Document) = documentDao.insertDocument(document)

    suspend fun deleteDocument(docId: String) = documentDao.deleteDocument(docId)

    suspend fun updateDocument(document: Document) = documentDao.updateDocument(document)

    suspend fun getDetailData(docId: String): Flow<APIResponse<MainListModel.DetailResponse>> {
        return flow {
            val localData = documentDao.getDocument(docId).firstOrNull()
            if (localData != null) {
                emit(
                    APIResponse.Success(
                        MainListModel.DetailResponse(
                            data = MainListModel.Data(
                                docId = localData.docId,
                                title = localData.title,
                                summary = localData.summary,
                                url = localData.url,
                                content = localData.linkData,
                                createdAt = localData.date,
                                updatedAt = localData.date,
                                tags = localData.tagList,
                                isSelected = false,
                                status = "Success",
                                type = "local",
                                thumbnailUrl = ""
                            ),
                            message = "Success"
                        )
                    )
                )
            } else {
                val response = api.getDetailData(docId)
                if (response.isSuccessful && response.body() != null) {
                    val detailResponse = response.body()!!.data
                    if (detailResponse.status == "COMPLETED") {
                        documentDao.insertDocument(
                            Document(
                                docId = docId,
                                date = detailResponse.createdAt,
                                title = detailResponse.title,
                                tagList = detailResponse.tags,
                                summary = detailResponse.summary,
                                linkData = detailResponse.content,
                                url = detailResponse.url,
                            )
                        )
                    }
                    emit(APIResponse.Success(response.body()!!))
                } else {
                    emit(
                        APIResponse.Error(
                            response.message(),
                            errorCode = response.code().toString()
                        )
                    )
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}