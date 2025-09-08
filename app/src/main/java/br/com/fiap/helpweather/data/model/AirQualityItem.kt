package br.com.fiap.helpweather.data.model

data class AirQualityItem(
    val main: AirQualityIndex,
    val components: Components,
    val dt: Long
)