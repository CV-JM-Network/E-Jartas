package com.jmnetwork.e_jartas.model

import com.google.gson.annotations.SerializedName

class Additional : ArrayList<AdditionalItem>()

data class AdditionalItem(
    @SerializedName("id") val id: Int,
    @SerializedName("parameter") val parameter: String,
    @SerializedName("value") val value: String
)
