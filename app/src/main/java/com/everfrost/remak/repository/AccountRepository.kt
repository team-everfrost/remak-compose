package com.everfrost.remak.repository

import com.everfrost.remak.dataSource.RemoteDataSource
import com.everfrost.remak.model.APIResponse
import com.everfrost.remak.model.account.SignInModel
import com.everfrost.remak.model.account.SignUpModel
import com.everfrost.remak.model.profile.UserModel


interface AccountRepository {
    suspend fun checkEmail(email: String): APIResponse<SignInModel.CheckEmailResponse>
    suspend fun signIn(email: String, password: String): APIResponse<SignInModel.ResponseBody>
    suspend fun getVerifyCode(email: String): APIResponse<SignUpModel.GetVerifyResponseBody>
    suspend fun checkVerifyCode(
        signupCode: String,
        email: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody>

    suspend fun resetPasswordCode(email: String): APIResponse<SignUpModel.GetVerifyResponseBody>
    suspend fun checkVerifyResetCode(
        signupCode: String,
        email: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody>

    suspend fun resetPassword(
        email: String,
        password: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody>

    suspend fun signUp(email: String, password: String): APIResponse<SignUpModel.SignUpResponseBody>
    suspend fun getUserData(): APIResponse<UserModel.Response>
    suspend fun getStorageSize(): APIResponse<UserModel.StorageData>
    suspend fun getStorageUsage(): APIResponse<UserModel.StorageData>

    suspend fun withdrawCode(): APIResponse<SignUpModel.WithdrawVerifyResponseBody>
    suspend fun verifyWithdrawCode(code: String): APIResponse<SignUpModel.WithdrawVerifyResponseBody>
    suspend fun withdraw(): APIResponse<SignUpModel.WithdrawVerifyResponseBody>
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

    override suspend fun getVerifyCode(email: String): APIResponse<SignUpModel.GetVerifyResponseBody> {
        try {
            val response = remoteDataSource.getVerifyCode(email)
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

    override suspend fun checkVerifyCode(
        signupCode: String,
        email: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody> {
        try {
            val response = remoteDataSource.checkVerifyCode(signupCode, email)
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

    override suspend fun resetPasswordCode(email: String): APIResponse<SignUpModel.GetVerifyResponseBody> {
        try {
            val response = remoteDataSource.resetPasswordCode(email)
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

    override suspend fun checkVerifyResetCode(
        signupCode: String,
        email: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody> {
        try {
            val response = remoteDataSource.checkVerifyResetCode(signupCode, email)
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

    override suspend fun resetPassword(
        email: String,
        password: String
    ): APIResponse<SignUpModel.CheckVerifyResponseBody> {
        try {
            val response = remoteDataSource.resetPassword(email, password)
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

    override suspend fun signUp(
        email: String,
        password: String
    ): APIResponse<SignUpModel.SignUpResponseBody> {
        try {
            val response = remoteDataSource.signUp(email, password)
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

    override suspend fun withdrawCode(): APIResponse<SignUpModel.WithdrawVerifyResponseBody> {
        try {
            val response = remoteDataSource.withdrawCode()
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

    override suspend fun verifyWithdrawCode(code: String): APIResponse<SignUpModel.WithdrawVerifyResponseBody> {
        try {
            val response = remoteDataSource.verifyWithdrawCode(code)
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

    override suspend fun withdraw(): APIResponse<SignUpModel.WithdrawVerifyResponseBody> {
        try {
            val response = remoteDataSource.withdraw()
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


