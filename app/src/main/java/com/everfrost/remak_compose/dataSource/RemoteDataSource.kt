package com.everfrost.remak_compose.dataSource

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.service.ApiService
import retrofit2.Response
import javax.inject.Inject


interface RemoteDataSource {
    //account--------------------------------------------------------------------------------------------
    suspend fun checkEmail(email: String): Response<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): Response<SignInModel.ResponseBody>


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

}