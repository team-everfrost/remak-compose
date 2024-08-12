package com.everfrost.remak_compose.service

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.home.add.CreateModel
import com.everfrost.remak_compose.model.home.detail.UpdateModel
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.file.UploadFileModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    //Account ---------------------------------------------------------
    //이메일 존재하는지 확인
    @POST("auth/check-email")
    suspend fun checkEmail(@Body body: SignInModel.CheckEmailRequest): retrofit2.Response<SignInModel.CheckEmailResponse>

    //로그인
    @POST("auth/local/login")
    suspend fun signIn(@Body body: SignInModel.RequestBody): retrofit2.Response<SignInModel.ResponseBody>

    //home ---------------------------------------------------------
    //메인 리스트
    @GET("document")
    suspend fun getMainList(
        @Query("cursor") cursor: String?,
        @Query("doc-id") docID: String?,
        @Query("limit") limit: Int? = 20
    ): retrofit2.Response<MainListModel.Response>

    //Document ---------------------------------------------------------
    //자료 상세
    @GET("document/{docId}")
    suspend fun getDetailData(@Path("docId") docId: String): retrofit2.Response<MainListModel.DetailResponse>

    //파일 다운로드
    @GET("document/file/{docId}")
    suspend fun downloadFile(@Path("docId") docId: String): retrofit2.Response<DownloadModel.ResponseBody>

    //파일 업로드
    @Multipart
    @POST("document/file")
    suspend fun uploadFile(@Part files: List<MultipartBody.Part>): retrofit2.Response<UploadFileModel.ResponseBody>

    //웹페이지 업로드
    @POST("document/webpage")
    suspend fun createWebPage(@Body body: CreateModel.WebPageRequestBody): retrofit2.Response<CreateModel.WebPageResponseBody>

    //메모 업로드
    //메모 생성
    @POST("document/memo")
    suspend fun createMemo(@Body body: CreateModel.MemoRequestBody): retrofit2.Response<CreateModel.MemoResponseBody>

    //메모 수정
    @PATCH("document/memo/{docId}")
    suspend fun updateMemo(
        @Path("docId") docId: String,
        @Body body: UpdateModel.MemoRequestBody
    ): retrofit2.Response<UpdateModel.MemoResponseBody>


}