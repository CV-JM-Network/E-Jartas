package com.jmnetwork.e_jartas.repository

import com.jmnetwork.e_jartas.model.DashboardResponse
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.LoginWebappResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>

    @Multipart
    @POST("admin/update/profile")
    fun updateProfile(
        @Part("idadmin") idadmin: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("telp") telp: RequestBody,
        @Part foto: MultipartBody.Part? = null,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("webapp")
    fun loginWebapp(
        @Field("idadmin") idadmin: String,
        @Field("device_id") deviceId: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginWebappResponse>

    @GET("dashboard")
    fun dashboard(
        @Header("Authorization") authorization: String,
    ): Call<DashboardResponse>
}