package br.com.fiap.helpweather.data.model

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)