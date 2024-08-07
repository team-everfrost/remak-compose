package com.everfrost.remak_compose.service

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.home.file.DownloadModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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


}