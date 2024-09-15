package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName


class Location : ArrayList<LocationItem>()
class Additional : ArrayList<AdditionalItem>()

data class DefaultResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)

data class LocationItem(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
)

data class AdditionalItem(
    @SerializedName("id") val id: Int,
    @SerializedName("parameter") val parameter: String,
    @SerializedName("value") val value: String
)

data class TotalData(
    @SerializedName("total_data") val totalData: Int,
    @SerializedName("total_panjang_km") val totalPanjangKm: Int,
    @SerializedName("total_panjang_meter") val totalPanjangMeter: Int
)