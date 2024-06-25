package com.jmnetwork.e_jartas.model


import com.google.gson.annotations.SerializedName

data class TotalData(
    @SerializedName("total_data") val totalData: Int,
    @SerializedName("total_panjang_km") val totalPanjangKm: Int,
    @SerializedName("total_panjang_meter") val totalPanjangMeter: Int
)