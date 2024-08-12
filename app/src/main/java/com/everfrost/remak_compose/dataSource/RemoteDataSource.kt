package com.everfrost.remak_compose.dataSource

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.collection.CollectionListModel
import com.everfrost.remak_compose.model.home.add.CreateModel
import com.everfrost.remak_compose.model.home.detail.UpdateModel
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.file.UploadFileModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.model.tag.TagListModel
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

    //메모 수정
    suspend fun updateMemo(
        docId: String,
        body: UpdateModel.MemoRequestBody
    ): Response<UpdateModel.MemoResponseBody>

    //tag--------------------------------------------------------------------------------------------
    //태그 리스트
    suspend fun getTagList(
        offset: Int?,
        query: String?
    ): Response<TagListModel.Response>

    //태그 상세
    suspend fun getTagDetailData(
        tagName: String,
        cursor: String?,
        docId: String?
    ): Response<MainListModel.Response>

    //collection--------------------------------------------------------------------------------------------
    //컬렉션 리스트
    suspend fun getCollectionList(

    ): Response<CollectionListModel.Response>


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

    //메모 수정
    override suspend fun updateMemo(
        docId: String,
        body: UpdateModel.MemoRequestBody
    ): Response<UpdateModel.MemoResponseBody> {
        return apiService.updateMemo(docId, body)
    }

    //tag--------------------------------------------------------------------------------------------
    //태그 리스트
    override suspend fun getTagList(offset: Int?, query: String?): Response<TagListModel.Response> {
        return apiService.getTagListData(limit = 20, offset, query = query)
    }

    //태그 상세
    override suspend fun getTagDetailData(
        tagName: String,
        cursor: String?,
        docId: String?
    ): Response<MainListModel.Response> {
        return apiService.getTagDetailData(tagName, cursor, docId)
    }


    //collection--------------------------------------------------------------------------------------------
    //컬렉션 리스트
    override suspend fun getCollectionList(

    ): Response<CollectionListModel.Response> {
        return apiService.getCollectionListData(offset = 0, limit = 20)
    }

}