package com.everfrost.remak.service

import com.everfrost.remak.model.DeleteModel
import com.everfrost.remak.model.account.SignInModel
import com.everfrost.remak.model.account.SignUpModel
import com.everfrost.remak.model.collection.AddDataInCollectionModel
import com.everfrost.remak.model.collection.CollectionListModel
import com.everfrost.remak.model.collection.CreateCollectionModel
import com.everfrost.remak.model.home.add.CreateModel
import com.everfrost.remak.model.home.detail.UpdateModel
import com.everfrost.remak.model.home.file.DownloadModel
import com.everfrost.remak.model.home.file.UploadFileModel
import com.everfrost.remak.model.home.main.MainListModel
import com.everfrost.remak.model.profile.UserModel
import com.everfrost.remak.model.tag.TagListModel
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    //인증번호 받기
    @POST("auth/signup-code")
    suspend fun getVerifyCode(@Body body: SignUpModel.GetVerifyRequestBody): retrofit2.Response<SignUpModel.GetVerifyResponseBody>

    //인증번호 확인
    @POST("auth/verify-code")
    suspend fun checkVerifyCode(@Body body: SignUpModel.CheckVerifyRequestBody): retrofit2.Response<SignUpModel.CheckVerifyResponseBody>

    //회원가입
    @POST("auth/local/signup")
    suspend fun signUp(@Body body: SignUpModel.SignUpRequestBody): retrofit2.Response<SignUpModel.SignUpResponseBody>

    //비밀번호 변경
    @POST("auth/reset-code")
    suspend fun resetPasswordCode(@Body body: SignUpModel.GetVerifyRequestBody): retrofit2.Response<SignUpModel.GetVerifyResponseBody>

    @POST("auth/verify-reset-code")
    suspend fun checkVerifyResetCode(@Body body: SignUpModel.CheckVerifyRequestBody): retrofit2.Response<SignUpModel.CheckVerifyResponseBody>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: SignUpModel.SignUpRequestBody): retrofit2.Response<SignUpModel.CheckVerifyResponseBody>

    //회원탈퇴
    @POST("auth/withdraw-code")
    suspend fun withdrawCode(): retrofit2.Response<SignUpModel.WithdrawVerifyResponseBody>

    @POST("auth/verify-withdraw-code")
    suspend fun verifyWithdrawCode(@Body body: SignUpModel.WithdrawVerifyRequestBody): retrofit2.Response<SignUpModel.WithdrawVerifyResponseBody>

    @POST("auth/withdraw")
    suspend fun withdraw(): retrofit2.Response<SignUpModel.WithdrawVerifyResponseBody>


    //로그인
    @POST("auth/local/login")
    suspend fun signIn(@Body body: SignInModel.RequestBody): retrofit2.Response<SignInModel.ResponseBody>

    //프로필 정보
    @GET("user")
    suspend fun getUserData(): retrofit2.Response<UserModel.Response>

    //최대 공간
    @GET("user/storage/size")
    suspend fun getStorageSize(): retrofit2.Response<UserModel.StorageData>

    //사용량
    @GET("user/storage/usage")
    suspend fun getStorageUsage(): retrofit2.Response<UserModel.StorageData>

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

    // 자료 삭제
    @DELETE("document/{docId}")
    suspend fun deleteDocument(@Path("docId") docId: String): retrofit2.Response<DeleteModel.ResponseBody>

    //tag ---------------------------------------------------------
    //태그 리스트
    @GET("tag")
    suspend fun getTagListData(
        @Query("limit") limit: Int? = 20,
        @Query("offset") offset: Int?,
        @Query("query") query: String?
    ): retrofit2.Response<TagListModel.Response>

    //태그 디테일 리스트
    @GET("document/search/tag")
    suspend fun getTagDetailData(
        @Query("tagName") tagName: String?,
        @Query("cursor") cursor: String?,
        @Query("doc-id") docID: String?,
        @Query("limit") limit: Int? = 20
    ): retrofit2.Response<MainListModel.Response>

    //collection ---------------------------------------------------------
    //컬렉션 리스트
    @GET("collection")
    suspend fun getCollectionListData(
        @Query("limit") limit: Int? = 20,
        @Query("offset") offset: Int?
    ): retrofit2.Response<CollectionListModel.Response>

    //컬렉션 추가
    @POST("collection")
    suspend fun createCollection(@Body body: CreateCollectionModel.RequestBody): retrofit2.Response<CreateCollectionModel.ResponseBody>

    //컬렉션 디테일 리스트
    @GET("document/search/collection")
    suspend fun getCollectionDetailData(
        @Query("collectionName") collectionName: String?,
        @Query("cursor") cursor: String?,
        @Query("doc-id") docID: String?,
        @Query("limit") limit: Int? = 20
    ): retrofit2.Response<MainListModel.Response>

    //컬렉션 수정
    @PATCH("collection/{name}")
    suspend fun updateCollection(
        @Path("name") name: String,
        @Body body: AddDataInCollectionModel.UpdateCollectionRequestBody
    ): retrofit2.Response<AddDataInCollectionModel.RemoveResponse>

    //컬렉션에 자료추가
    @PATCH("collection/{name}")
    suspend fun addDataInCollection(
        @Path("name") name: String,
        @Body body: AddDataInCollectionModel.AddRequestBody
    ): retrofit2.Response<AddDataInCollectionModel.AddResponse>

    //컬렉션에서 자료 삭제
    @PATCH("collection/{name}")
    suspend fun removeDataInCollection(
        @Path("name") name: String,
        @Body body: AddDataInCollectionModel.RemoveRequestBody
    ): retrofit2.Response<AddDataInCollectionModel.RemoveResponse>

    //컬렉션 삭제
    @DELETE("collection/{name}")
    suspend fun deleteCollection(@Path("name") name: String): retrofit2.Response<DeleteModel.ResponseBody>

    // search ---------------------------------------------------------
    // 텍스트검색
    @GET("document/search/text")
    suspend fun getTextSearchData(
        @Query("query") query: String?,
        @Query("limit") limit: Int? = 20,
        @Query("offset") offset: Int?
    ): retrofit2.Response<MainListModel.Response>

    // 임베딩 검색
    @GET("document/search/hybrid")
    suspend fun getEmbeddingData(
        @Query("query") query: String?,
    ): retrofit2.Response<MainListModel.Response>


}