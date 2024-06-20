package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("data") val data: DasboardModel,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("tokenAuth") val tokenAuth: String
)

data class DasboardModel(
    @SerializedName("tiang_terdata") val tiangTerdata: Int,
    @SerializedName("titik_tiang") val titikTiang: Int,
    @SerializedName("qrcode_belum_terdata") val qrcodeBelumTerdata: Int,
    @SerializedName("total_qrcode") val totalQrcode: Int,
    @SerializedName("jumlah_ruas_jalan") val jumlahRuasJalan: Int,
    @SerializedName("jumlah_provider") val jumlahProvider: Int,
    @SerializedName("panjang_ruas_jalan_km") val panjangRuasJalan: Int
)
