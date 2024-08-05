package com.everfrost.remak_compose.dataSource

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.home.main.MainListModel
import com.everfrost.remak_compose.service.ApiService
import retrofit2.Response
import javax.inject.Inject


interface RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): Response<SignInModel.ResponseBody>

    //home--------------------------------------------------------------------------------------------
    suspend fun getMainList(cursor: String?, docID: String?): Response<MainListModel.Response>


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

}