package com.everfrost.remak_compose.dataSource

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.home.add.CreateModel
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.file.UploadFileModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.service.ApiService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


interface RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): Response<SignInModel.ResponseBody>

    //home--------------------------------------------------------------------------------------------
    suspend fun getMainList(cursor: String?, docID: String?): Response<MainListModel.Response>

    //Document--------------------------------------------------------------------------------------------
    //자료 상세
    suspend fun getDetailData(docId: String): Response<MainListModel.DetailResponse>

    //파일 다운로드
    suspend fun downloadFile(docId: String): Response<DownloadModel.ResponseBody>

    //파입 업로드
    suspend fun uploadFile(files: List<MultipartBody.Part>): Response<UploadFileModel.ResponseBody>

    //웹페이지 업로드
    suspend fun createWebPage(url: String): Response<CreateModel.WebPageResponseBody>

    //메모 업로드
    suspend fun createMemo(body: CreateModel.MemoRequestBody): Response<CreateModel.MemoResponseBody>


}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    override suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse> {
        val requestBody = SignInModel.CheckEmailRequest(email = email)
        return apiService.checkEmail(requestBody)
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Response<SignInModel.ResponseBody> {
        val requestBody = SignInModel.RequestBody(email = email, password = password)
        return apiService.signIn(requestBody)
    }

    //home--------------------------------------------------------------------------------------------
    override suspend fun getMainList(
        cursor: String?,
        docID: String?
    ): Response<MainListModel.Response> {
        return apiService.getMainList(cursor, docID)
    }

    //Document--------------------------------------------------------------------------------------------
    //자료 상세
    override suspend fun getDetailData(docId: String): Response<MainListModel.DetailResponse> {
        return apiService.getDetailData(docId)
    }

    //파일 다운로드
    override suspend fun downloadFile(docId: String): Response<DownloadModel.ResponseBody> {
        return apiService.downloadFile(docId)
    }

    //파입 업로드
    override suspend fun uploadFile(files: List<MultipartBody.Part>): Response<UploadFileModel.ResponseBody> {
        return apiService.uploadFile(files)
    }

    //웹페이지 업로드
    override suspend fun createWebPage(url: String): Response<CreateModel.WebPageResponseBody> {
        val requestBody = CreateModel.WebPageRequestBody(" ", url = url, " ")
        return apiService.createWebPage(requestBody)
    }

    //메모 업로드
    override suspend fun createMemo(body: CreateModel.MemoRequestBody): Response<CreateModel.MemoResponseBody> {
        return apiService.createMemo(body)
    }

}