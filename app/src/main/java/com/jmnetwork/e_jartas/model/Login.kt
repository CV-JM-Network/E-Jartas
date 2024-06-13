package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("data") val data: UserModel,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("tokenAuth") val tokenAuth: String
)

data class UserModel(
    @SerializedName("alamat") val alamat: String,
    @SerializedName("device_token") val deviceToken: Any?,
    @SerializedName("email") val email: String,
    @SerializedName("idadmin") val idadmin: Int,
    @SerializedName("idlevel") val idlevel: Int,
    @SerializedName("img") val img: String,
    @SerializedName("level") val level: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("telp") val telp: String
)