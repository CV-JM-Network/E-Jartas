package com.jmnetwork.e_jartas.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import org.json.JSONObject

data class RuasJalanResponse(
    @SerializedName("current_page") val currentPage: String,
    @SerializedName("data") val data: List<RuasJalanData>,
    @SerializedName("data_tabel") val dataTabel: String,
    @SerializedName("limit") val limit: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalData") val totalData: TotalData
)

data class RuasJalanData(
    @SerializedName("additional") val additional: String?,
    @SerializedName("createddate") val createdDate: String,
    @SerializedName("desa") val desa: String,
    @SerializedName("fungsi") val fungsi: String,
    @SerializedName("idruas_jalan") val idRuasJalan: Int,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("lastupdate") val lastUpdate: String,
    @SerializedName("latlong") val latLong: String?,
    @SerializedName("nama_ruas_jalan") val namaRuasJalan: String,
    @SerializedName("no_ruas") val noRuas: String,
    @SerializedName("oleh") val oleh: Int,
    @SerializedName("panjang") val panjang: String,
    @SerializedName("status") val status: String,
    @SerializedName("tipe") val tipe: String
)

@Parcelize
data class RuasJalanRequest(
    @SerializedName("idruas_jalan") val idRuasJalan: Int,
    @SerializedName("no_ruas") val noRuas: String,
    @SerializedName("nama_ruas_jalan") val namaRuasJalan: String,
    @SerializedName("desa") val desa: String,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("panjang") val panjang: String,
    @SerializedName("status") val status: String,
    @SerializedName("tipe") val tipe: String,
    @SerializedName("fungsi") val fungsi: String,
    @SerializedName("latlong") val latLong: List<Location>,
    @SerializedName("additional") val additional: @RawValue List<JSONObject>
) : Parcelable

@Parcelize
data class Location(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
) : Parcelable