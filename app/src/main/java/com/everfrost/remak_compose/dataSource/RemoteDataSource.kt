package com.everfrost.remak_compose.dataSource

import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.service.ApiService
import retrofit2.Response
import javax.inject.Inject


interface RemoteDataSource {
    suspend fun signIn(email: String, password: String): Response<SignInModel.ResponseBody>


}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService

) : RemoteDataSource {
    override suspend fun signIn(
        email: String,
        password: String
    ): Response<SignInModel.ResponseBody> {
        val requestBody = SignInModel.RequestBody(email = email, password = password)
        return apiService.signIn(requestBody)
    }

}