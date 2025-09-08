package br.com.fiap.helpweather.util

fun aqiDescription(aqi: Int?): String {
    return when (aqi) {
        1 -> "Muito Bom"
        2 -> "Bom"
        3 -> "Mediano"
        4 -> "Ruim"
        5 -> "Muito Ruim"
        else -> "N/A"
    }
}
