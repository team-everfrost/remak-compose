package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.DeleteModel
import com.everfrost.remak_compose.model.home.add.CreateModel
import com.everfrost.remak_compose.model.home.detail.UpdateModel
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.file.UploadFileModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import okhttp3.MultipartBody
import retrofit2.Response


interface DocumentRepository {
    //Document--------------------------------------------------------------------------------------------
    //자료 상세
    suspend fun getDetailData(docId: String): APIResponse<MainListModel.DetailResponse>

    //파일 다운로드
    suspend fun downloadFile(docId: String): APIResponse<DownloadModel.ResponseBody>

    //파일 업로드
    suspend fun uploadFile(files: List<MultipartBody.Part>): APIResponse<UploadFileModel.ResponseBody>

    //웹페이지 업로드
    suspend fun createWebPage(url: String): APIResponse<CreateModel.WebPageResponseBody>

    //메모 업로드
    suspend fun createMemo(body: CreateModel.MemoRequestBody): APIResponse<CreateModel.MemoResponseBody>

    //메모 수정
    suspend fun updateMemo(
        docId: String,
        body: UpdateModel.MemoRequestBody
    ): APIResponse<UpdateModel.MemoResponseBody>

    //자료 삭제
    suspend fun deleteDocument(docId: String): APIResponse<DeleteModel.ResponseBody>

}

class DocumentRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : DocumentRepository {
    //Document--------------------------------------------------------------------------------------------
    //자료 상세
    override suspend fun getDetailData(docId: String): APIResponse<MainListModel.DetailResponse> {
        try {
            val response = remoteDataSource.getDetailData(docId)
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

    //파일 다운로드
    override suspend fun downloadFile(docId: String): APIResponse<DownloadModel.ResponseBody> {
        try {
            val response = remoteDataSource.downloadFile(docId)
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

    //파일 업로드
    override suspend fun uploadFile(files: List<MultipartBody.Part>): APIResponse<UploadFileModel.ResponseBody> {
        try {
            val response = remoteDataSource.uploadFile(files)
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

    //웹페이지 업로드
    override suspend fun createWebPage(url: String): APIResponse<CreateModel.WebPageResponseBody> {
        try {
            val response = remoteDataSource.createWebPage(url)
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

    //메모 업로드
    override suspend fun createMemo(body: CreateModel.MemoRequestBody): APIResponse<CreateModel.MemoResponseBody> {
        try {
            val response = remoteDataSource.createMemo(body)
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

    //메모 수정
    override suspend fun updateMemo(
        docId: String,
        body: UpdateModel.MemoRequestBody
    ): APIResponse<UpdateModel.MemoResponseBody> {
        try {
            val response = remoteDataSource.updateMemo(docId, body)
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

    //자료 삭제
    override suspend fun deleteDocument(docId: String): APIResponse<DeleteModel.ResponseBody> {
        try {
            val response = remoteDataSource.deleteDocument(docId)
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