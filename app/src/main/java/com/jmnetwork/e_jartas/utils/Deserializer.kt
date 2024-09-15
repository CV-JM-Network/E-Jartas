package com.jmnetwork.e_jartas.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.model.AdditionalItem
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.model.LocationItem
import java.lang.reflect.Type

class AdditionalDeserializer : JsonDeserializer<Additional> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Additional {
        val additional = Additional()
        // Check if json is not null and is a JSON array string
        if (json != null && json.isJsonPrimitive) {
            val jsonString = json.asString
            // Parse the string-escaped JSON into a list of AdditionalItems
            val jsonArray = JsonParser.parseString(jsonString).asJsonArray
            jsonArray.forEach { it ->
                val item = context?.deserialize<AdditionalItem>(it, AdditionalItem::class.java)
                item?.let { additional.add(it) }
            }
        }
        return additional
    }
}

class LocationDeserializer : JsonDeserializer<Location> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Location {
        val location = Location()
        // Check if json is not null and is a JSON array string
        if (json != null && json.isJsonPrimitive) {
            val jsonString = json.asString
            // Parse the string-escaped JSON into a list of LocationItems
            val jsonArray = JsonParser.parseString(jsonString).asJsonArray
            jsonArray.forEach { it ->
                val item = context?.deserialize<LocationItem>(it, LocationItem::class.java)
                item?.let { location.add(it) }
            }
        }
        return location
    }
}