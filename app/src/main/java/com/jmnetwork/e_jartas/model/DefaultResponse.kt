package com.jmnetwork.e_jartas.model


import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)