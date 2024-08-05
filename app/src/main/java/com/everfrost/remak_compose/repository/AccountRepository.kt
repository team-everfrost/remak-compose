package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.account.SignInModel


interface AccountRepository {
    suspend fun checkEmail(email: String): APIResponse<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): APIResponse<SignInModel.ResponseBody>

}

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : AccountRepository {
    override suspend fun checkEmail(email: String): APIResponse<SignInModel.CheckEmailResponse> {
        try {
            val response = remoteDataSource.checkEmail(email)
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

    override suspend fun signIn(
        email: String,
        password: String
    ): APIResponse<SignInModel.ResponseBody> {
        try {
            val response = remoteDataSource.signIn(email, password)
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


