package com.jmnetwork.e_jartas.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.model.AdditionalItem
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