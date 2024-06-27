package com.jmnetwork.e_jartas.model


import com.google.gson.annotations.SerializedName
import org.json.JSONObject

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

//{
//    "no_ruas": 123,
//    "nama_ruas_jalan": "Jalan Pelan pelan banyak anak kecil",
//    "desa": "Desa A",
//    "kecamatan": "Kecamatan X",
//    "panjang": "5",
//    "status": "Baik",
//    "tipe": "Aspal",
//    "fungsi": "Utama",
//    "latlong": [{"lat":"-8.330878572003654","lng":"114.3628407089844"}],
//    "additional": [{"id":0,"parameter":"asolole","value":"iwak peyek"}]
//}

data class RuasJalanRequest(
    @SerializedName("no_ruas") val noRuas: String,
    @SerializedName("nama_ruas_jalan") val namaRuasJalan: String,
    @SerializedName("desa") val desa: String,
    @SerializedName("kecamatan") val kecamatan: String,
    @SerializedName("panjang") val panjang: String,
    @SerializedName("status") val status: String,
    @SerializedName("tipe") val tipe: String,
    @SerializedName("fungsi") val fungsi: String,
    @SerializedName("latlong") val latlong: List<Location>,
    @SerializedName("additional") val additional: List<JSONObject>
)

data class Location(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
)