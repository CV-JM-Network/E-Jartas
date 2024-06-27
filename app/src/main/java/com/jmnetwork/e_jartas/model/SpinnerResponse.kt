package com.jmnetwork.e_jartas.model


import com.google.gson.annotations.SerializedName

data class SpinnerResponse(
    @SerializedName("data") val data: List<Data>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("varian") val varian: String
)

data class Data(
    @SerializedName("desa") val desa: String,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("status") val status: String,
    @SerializedName("tipe") val tipe: String,
    @SerializedName("fungsi") val fungsi: String,
    @SerializedName("total_ruas_jalan") val totalRuasJalan: Int
)