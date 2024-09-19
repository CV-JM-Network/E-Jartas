package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName

data class TiangResponse(
    @SerializedName("current_page") val currentPage: String,
    @SerializedName("data") val data: List<TiangData>,
    @SerializedName("data_tabel") val dataTabel: String,
    @SerializedName("limit") val limit: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalData") val totalData: TotalData
)

data class TiangData(
    @SerializedName("createddate") val createddate: String,
    @SerializedName("idtiang") val idtiang: Int,
    @SerializedName("idtiang_cetak_qrcode") val idtiangCetakQrcode: Int,
    @SerializedName("json_provider") val tiangProvider: TiangProvider,
    @SerializedName("jumlah_tiang") val jumlahTiang: String,
    @SerializedName("lastupdate") val lastupdate: String,
    @SerializedName("latlong") val latlong: Location,
    @SerializedName("oleh") val oleh: Int,
    @SerializedName("petugas_scan") val petugasScan: Any?,
    @SerializedName("qrcode") val qrcode: String,
)

class TiangProvider : ArrayList<TiangProviderItem>()

data class TiangProviderItem(
    @SerializedName("catatan") val catatan: String,
    @SerializedName("id") val id: Int,
    @SerializedName("idprovider") val idprovider: String,
    @SerializedName("layak") val layak: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("utilitas") val utilitas: String
)