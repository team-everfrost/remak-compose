package com.everfrost.remak_compose.service

import com.everfrost.remak_compose.model.account.SignInModel
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    //로그인
    @POST("auth/local/login")
    suspend fun signIn(@Body body: SignInModel.RequestBody): retrofit2.Response<SignInModel.ResponseBody>


}