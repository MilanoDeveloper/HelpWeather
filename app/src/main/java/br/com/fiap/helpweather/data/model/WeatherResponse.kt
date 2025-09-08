package br.com.fiap.helpweather.data.model

data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val coord: Coord
)