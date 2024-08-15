package com.everfrost.remak.dataSource

import android.util.Log
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
import com.everfrost.remak.service.ApiService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


interface RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): Response<SignInModel.ResponseBody>
    suspend fun getVerifyCode(email: String): Response<SignUpModel.GetVerifyResponseBody>
    suspend fun checkVerifyCode(
        signupCode: String,
        email: String
    ): Response<SignUpModel.CheckVerifyResponseBody>

    suspend fun resetPasswordCode(email: String): Response<SignUpModel.GetVerifyResponseBody>
    suspend fun checkVerifyResetCode(
        signupCode: String,
        email: String
    ): Response<SignUpModel.CheckVerifyResponseBody>

    suspend fun resetPassword(
        email: String,
        password: String
    ): Response<SignUpModel.CheckVerifyResponseBody>

    suspend fun signUp(email: String, password: String): Response<SignUpModel.SignUpResponseBody>
    suspend fun getUserData(): Response<UserModel.Response>
    suspend fun getStorageSize(): Response<UserModel.StorageData>
    suspend fun getStorageUsage(): Response<UserModel.StorageData>

    //회원탈퇴
    suspend fun withdrawCode(): Response<SignUpModel.WithdrawVerifyResponseBody>
    suspend fun verifyWithdrawCode(code: String): Response<SignUpModel.WithdrawVerifyResponseBody>
    suspend fun withdraw(): Response<SignUpModel.WithdrawVerifyResponseBody>

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

    //자료 삭제
    suspend fun deleteDocument(docId: String): Response<DeleteModel.ResponseBody>

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

    //컬렉션 추가
    suspend fun createCollection(
        name: String,
        description: String?
    ): Response<CreateCollectionModel.ResponseBody>

    //컬렉션 디테일 리스트
    suspend fun getCollectionDetailList(
        collectionId: String,
        cursor: String?,
        docId: String?
    ): Response<MainListModel.Response>

    //컬렉션 수정
    suspend fun updateCollection(
        name: String,
        newName: String,
        description: String?
    ): Response<AddDataInCollectionModel.RemoveResponse>

    //컬렉션에 자료추가
    suspend fun addDataInCollection(
        name: String,
        docIds: List<String>
    ): Response<AddDataInCollectionModel.AddResponse>

    //컬렉션에서 자료 삭제
    suspend fun removeDataInCollection(
        name: String,
        docIds: List<String>
    ): Response<AddDataInCollectionModel.RemoveResponse>

    //컬렉션 삭제
    suspend fun deleteCollection(name: String): Response<DeleteModel.ResponseBody>

    // search--------------------------------------------------------------------------------------------
    //텍스트검색
    suspend fun getTextSearchData(
        query: String,
        offset: Int?
    ): Response<MainListModel.Response>

    //임베딩 검색
    suspend fun getEmbeddingSearchData(
        query: String,
    ): Response<MainListModel.Response>
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    override suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse> {
        val requestBody = SignInModel.CheckEmailRequest(email = email)
        Log.d("checkEmail", requestBody.toString())
        return apiService.checkEmail(requestBody)
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Response<SignInModel.ResponseBody> {
        val requestBody = SignInModel.RequestBody(email = email, password = password)
        return apiService.signIn(requestBody)
    }

    override suspend fun getVerifyCode(email: String): Response<SignUpModel.GetVerifyResponseBody> {
        val requestBody = SignUpModel.GetVerifyRequestBody(email)
        return apiService.getVerifyCode(requestBody)
    }

    override suspend fun checkVerifyCode(
        signupCode: String,
        email: String
    ): Response<SignUpModel.CheckVerifyResponseBody> {
        val requestBody = SignUpModel.CheckVerifyRequestBody(signupCode, email)
        return apiService.checkVerifyCode(requestBody)
    }

    override suspend fun resetPasswordCode(email: String): Response<SignUpModel.GetVerifyResponseBody> {
        val requestBody = SignUpModel.GetVerifyRequestBody(email)
        return apiService.resetPasswordCode(requestBody)
    }

    override suspend fun checkVerifyResetCode(
        signupCode: String,
        email: String
    ): Response<SignUpModel.CheckVerifyResponseBody> {
        val requestBody = SignUpModel.CheckVerifyRequestBody(signupCode, email)
        return apiService.checkVerifyResetCode(requestBody)
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Response<SignUpModel.CheckVerifyResponseBody> {
        val requestBody = SignUpModel.SignUpRequestBody(email, password)
        return apiService.resetPassword(requestBody)
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Response<SignUpModel.SignUpResponseBody> {
        val requestBody = SignUpModel.SignUpRequestBody(email, password)
        return apiService.signUp(requestBody)
    }

    override suspend fun getUserData(): Response<UserModel.Response> {
        return apiService.getUserData()
    }

    override suspend fun getStorageSize(): Response<UserModel.StorageData> {
        return apiService.getStorageSize()
    }

    override suspend fun getStorageUsage(): Response<UserModel.StorageData> {
        return apiService.getStorageUsage()
    }

    //회원탈퇴
    override suspend fun withdrawCode(): Response<SignUpModel.WithdrawVerifyResponseBody> {
        return apiService.withdrawCode()
    }

    override suspend fun verifyWithdrawCode(code: String): Response<SignUpModel.WithdrawVerifyResponseBody> {
        val requestBody = SignUpModel.WithdrawVerifyRequestBody(code)
        return apiService.verifyWithdrawCode(requestBody)
    }

    override suspend fun withdraw(): Response<SignUpModel.WithdrawVerifyResponseBody> {
        return apiService.withdraw()
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

    //자료 삭제
    override suspend fun deleteDocument(docId: String): Response<DeleteModel.ResponseBody> {
        return apiService.deleteDocument(docId)
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

    //컬렉션 추가
    override suspend fun createCollection(
        name: String,
        description: String?
    ): Response<CreateCollectionModel.ResponseBody> {
        val requestBody = CreateCollectionModel.RequestBody(name = name, description = description)
        return apiService.createCollection(requestBody)
    }

    //컬렉션 디테일 리스트
    override suspend fun getCollectionDetailList(
        collectionId: String,
        cursor: String?,
        docId: String?
    ): Response<MainListModel.Response> {
        return apiService.getCollectionDetailData(collectionId, cursor, docId)
    }

    //컬렉션 수정
    override suspend fun updateCollection(
        name: String,
        newName: String,
        description: String?
    ): Response<AddDataInCollectionModel.RemoveResponse> {
        val requestBody = AddDataInCollectionModel.UpdateCollectionRequestBody(newName, description)
        return apiService.updateCollection(name, requestBody)
    }

    //컬렉션에 자료추가
    override suspend fun addDataInCollection(
        name: String,
        docIds: List<String>
    ): Response<AddDataInCollectionModel.AddResponse> {
        val requestBody = AddDataInCollectionModel.AddRequestBody(docIds)
        return apiService.addDataInCollection(name, requestBody)
    }

    //컬렉션에서 자료 삭제
    override suspend fun removeDataInCollection(
        name: String,
        docIds: List<String>
    ): Response<AddDataInCollectionModel.RemoveResponse> {
        val requestBody = AddDataInCollectionModel.RemoveRequestBody(docIds)
        return apiService.removeDataInCollection(name, requestBody)
    }

    //컬렉션 삭제
    override suspend fun deleteCollection(name: String): Response<DeleteModel.ResponseBody> {
        return apiService.deleteCollection(name)
    }

    // search--------------------------------------------------------------------------------------------
    //텍스트검색
    override suspend fun getTextSearchData(
        query: String,
        offset: Int?
    ): Response<MainListModel.Response> {
        return apiService.getTextSearchData(query, offset = offset)
    }

    //임베딩 검색
    override suspend fun getEmbeddingSearchData(
        query: String,
    ): Response<MainListModel.Response> {
        return apiService.getEmbeddingData(query)
    }


}