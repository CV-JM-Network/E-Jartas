package com.jmnetwork.e_jartas.utils

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray

class Utils {
    fun capitalizeFirstCharacter(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
    }

    fun latLongConverter(latLong: String): LatLng? {
        if (latLong.isEmpty()) return null

        val rawLatLng = JSONArray(latLong).getJSONObject(0)
        val lat = rawLatLng.getString("lat").toDouble()
        val lng = rawLatLng.getString("lng").toDouble()
        return LatLng(lat, lng)
    }
}