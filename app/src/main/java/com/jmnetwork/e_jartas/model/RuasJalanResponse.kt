package com.jmnetwork.e_jartas.model


import com.google.gson.annotations.SerializedName

data class RuasJalanResponse(
    @SerializedName("data") val data: List<RuasJalanData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalData") val totalData: TotalData
)

data class RuasJalanData(
    @SerializedName("additional") val additional: String?,
    @SerializedName("createddate") val createddate: String,
    @SerializedName("desa") val desa: String,
    @SerializedName("fungsi") val fungsi: String,
    @SerializedName("idruas_jalan") val idruasJalan: Int,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("lastupdate") val lastupdate: String,
    @SerializedName("latlong") val latlong: String?,
    @SerializedName("nama_ruas_jalan") val namaRuasJalan: String,
    @SerializedName("no_ruas") val noRuas: String,
    @SerializedName("oleh") val oleh: Int,
    @SerializedName("panjang") val panjang: String,
    @SerializedName("status") val status: String,
    @SerializedName("tipe") val tipe: String
)