package br.com.fiap.helpweather.data.model

data class Components(
    val co: Double,     // Monóxido de carbono
    val no: Double,     // Óxido nítrico
    val no2: Double,    // Dióxido de nitrogênio
    val o3: Double,     // Ozônio
    val so2: Double,    // Dióxido de enxofre
    val pm2_5: Double,  // Material particulado 2.5
    val pm10: Double,   // Material particulado 10
    val nh3: Double     // Amônia
)