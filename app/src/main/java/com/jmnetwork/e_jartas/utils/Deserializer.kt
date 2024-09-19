package com.jmnetwork.e_jartas.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.model.TiangProvider
import java.lang.reflect.Type

inline fun <reified T> parseJsonArray(json: JsonElement?, context: JsonDeserializationContext?, list: MutableList<T>): MutableList<T> {
    if (json != null && json.isJsonPrimitive) {
        val jsonString = json.asString
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        jsonArray.forEach { jsonElement ->
            val item = context?.deserialize<T>(jsonElement, T::class.java)
            item?.let { list.add(it) }
        }
    }
    return list
}

class AdditionalDeserializer : JsonDeserializer<Additional> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Additional {
        val additional = Additional()
        parseJsonArray(json, context, additional) // Reuse the generic function
        return additional
    }
}

class LocationDeserializer : JsonDeserializer<Location> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Location {
        val location = Location()
        parseJsonArray(json, context, location) // Reuse the generic function
        return location
    }
}

class TiangProviderDeserializer : JsonDeserializer<TiangProvider> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TiangProvider {
        val tiangProvider = TiangProvider()
        parseJsonArray(json, context, tiangProvider) // Reuse the generic function
        return tiangProvider
    }
}
