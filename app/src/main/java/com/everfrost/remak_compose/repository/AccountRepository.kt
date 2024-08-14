package com.everfrost.remak_compose.repository

import com.everfrost.remak_compose.dataSource.RemoteDataSource
import com.everfrost.remak_compose.model.APIResponse
import com.everfrost.remak_compose.model.account.SignInModel
import com.everfrost.remak_compose.model.profile.UserModel


interface AccountRepository {
    suspend fun checkEmail(email: String): APIResponse<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): APIResponse<SignInModel.ResponseBody>
    suspend fun getUserData(): APIResponse<UserModel.Response>
    suspend fun getStorageSize(): APIResponse<UserModel.StorageData>
    suspend fun getStorageUsage(): APIResponse<UserModel.StorageData>

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

    override suspend fun getUserData(): APIResponse<UserModel.Response> {
        try {
            val response = remoteDataSource.getUserData()
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

    override suspend fun getStorageSize(): APIResponse<UserModel.StorageData> {
        try {
            val response = remoteDataSource.getStorageSize()
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

    override suspend fun getStorageUsage(): APIResponse<UserModel.StorageData> {
        try {
            val response = remoteDataSource.getStorageUsage()
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


