package br.com.fiap.helpweather.viewmodel

import androidx.lifecycle.*
import br.com.fiap.helpweather.data.model.AirQualityResponse
import br.com.fiap.helpweather.data.model.WeatherResponse
import br.com.fiap.helpweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class MapViewModel(private val repo: WeatherRepository) : ViewModel() {
    val weather = MutableLiveData<WeatherResponse>()
    val air = MutableLiveData<AirQualityResponse>()
    val error = MutableLiveData<String?>()

    fun load(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val w = repo.getCurrentWeather(city, apiKey)
                weather.postValue(w)
                val a = repo.getAirQuality(w.coord.lat, w.coord.lon, apiKey)
                air.postValue(a)
                error.postValue(null)
            } catch (e: Exception) {
                error.postValue("Falha no mapa: ${e.message}")
            }
        }
    }

    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(MapViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return MapViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM")
        }
    }
}
