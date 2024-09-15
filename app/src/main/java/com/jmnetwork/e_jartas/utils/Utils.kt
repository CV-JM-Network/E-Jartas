package com.jmnetwork.e_jartas.utils

import com.google.android.gms.maps.model.LatLng
import com.jmnetwork.e_jartas.model.Location

class Utils {
    fun capitalizeFirstCharacter(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
    }

    fun latLongConverter(latLong: Location): LatLng? {
        if (latLong.isEmpty()) return null

        val rawLatLng = latLong[0]
        val lat = rawLatLng.lat.toDouble()
        val lng = rawLatLng.lng.toDouble()
        return LatLng(lat, lng)
    }
}