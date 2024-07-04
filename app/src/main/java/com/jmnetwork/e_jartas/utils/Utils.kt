package com.jmnetwork.e_jartas.utils

class Utils {
    fun capitalizeFirstCharacter(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
    }
}