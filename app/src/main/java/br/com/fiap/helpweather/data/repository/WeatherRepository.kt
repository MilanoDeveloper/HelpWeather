package br.com.fiap.helpweather.data.repository

import br.com.fiap.helpweather.data.api.WeatherApi
import br.com.fiap.helpweather.data.model.WeatherResponse
import br.com.fiap.helpweather.data.model.ForecastResponse
import br.com.fiap.helpweather.data.model.AirQualityResponse

class WeatherRepository(private val api: WeatherApi) {

    suspend fun getCurrentWeather(city: String, apiKey: String): WeatherResponse {
        return api.getCurrentWeather(city, apiKey)
    }

    suspend fun getForecast(city: String, apiKey: String): ForecastResponse {
        return api.getForecast(city, apiKey)
    }

    suspend fun getAirQuality(lat: Double, lon: Double, apiKey: String): AirQualityResponse {
        return api.getAirQuality(lat, lon, apiKey)
    }
}
