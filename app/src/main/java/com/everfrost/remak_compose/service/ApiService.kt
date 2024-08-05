package com.everfrost.remak_compose.service

import com.everfrost.remak_compose.model.account.SignInModel
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    //Account ---------------------------------------------------------
    //이메일 존재하는지 확인
    @POST("auth/check-email")
    suspend fun checkEmail(@Body body: SignInModel.CheckEmailRequest): retrofit2.Response<SignInModel.CheckEmailResponse>

    //로그인
    @POST("auth/local/login")
    suspend fun signIn(@Body body: SignInModel.RequestBody): retrofit2.Response<SignInModel.ResponseBody>


}