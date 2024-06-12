package com.jmnetwork.e_jartas.retrofit

import com.jmnetwork.e_jartas.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>
}