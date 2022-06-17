package com.iprism.elliot.util

object Utils {
    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
        if (word.lowercase() != "and") word.lowercase()
            .replaceFirstChar { it.uppercase() } else word.lowercase()
    }
}