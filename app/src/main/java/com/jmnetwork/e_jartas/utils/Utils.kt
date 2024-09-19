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

    fun latLongConverter(latLong: Location?): LatLng? {
        if (latLong.isNullOrEmpty()) return null

        val rawLatLng = latLong[0]
        val lat = rawLatLng.lat.toDouble()
        val lng = rawLatLng.lng.toDouble()
        return LatLng(lat, lng)
    }

    fun formatDate(date: String): String {
        val dateArray = date.split(" ")
        val dateArray2 = dateArray[0].split("-")
        return "${dateArray2[2]}-${dateArray2[1]}-${dateArray2[0]}"
    }
}