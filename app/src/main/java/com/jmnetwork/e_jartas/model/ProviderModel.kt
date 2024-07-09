package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ProviderResponse(
    @SerializedName("current_page") val currentPage: String,
    @SerializedName("data") val data: List<ProviderData>,
    @SerializedName("data_tabel") val dataTabel: String,
    @SerializedName("limit") val limit: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalData") val totalData: TotalData
)

data class ProviderData(
    @SerializedName("additional") val additional: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("black_list") val blackList: String,
    @SerializedName("idprovider") val idProvider: Int,
    @SerializedName("jumlah_kepemilikan_tiang") val jumlahKepemilikanTiang: Int,
    @SerializedName("oleh") val oleh: Int,
    @SerializedName("provider") val provider: String,
    @SerializedName("status") val status: String
)

data class ProviderRequest(
    @SerializedName("additional") val additional: List<JSONObject>,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("provider") val provider: String
)