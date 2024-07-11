package com.jmnetwork.e_jartas.repository

import com.jmnetwork.e_jartas.model.DashboardResponse
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.LoginWebappResponse
import com.jmnetwork.e_jartas.model.SpinnerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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

    @GET("get/data")
    fun getAllData(
        @Query("limit") limit: Int,
        @Query("current_page") page: Int,
        @Query("data_tabel") tabel: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("get/varian/ruas_jalan")
    fun getSpinnerData(
        @Query("varian") varian: String,
        @Header("Authorization") authorization: String
    ): Call<SpinnerResponse>

    @FormUrlEncoded
    @POST("ruas_jalan/post/data")
    fun ruasJalan(
        @Field("action") action: String,
        @Field("iddata") iddata: Int?,
        @Field("data") data: JSONObject?,
        @Field("oleh") idadmin: Int,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("provider/post/data")
    fun provider(
        @Field("action") action: String,
        @Field("iddata") iddata: Int?,
        @Field("data") data: JSONObject?,
        @Field("oleh") idadmin: Int,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("provider/post/data/blacklist")
    fun blacklistProvider(
        @Field("oleh") idadmin: Int,
        @Field("idprovider") iddata: Int,
        @Field("black_list") blackList: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>
}