package br.com.fiap.helpweather.viewmodel

import androidx.lifecycle.*
import br.com.fiap.helpweather.data.model.AirQualityResponse
import br.com.fiap.helpweather.data.model.WeatherResponse
import br.com.fiap.helpweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _airQuality = MutableLiveData<AirQualityResponse>()
    val airQuality: LiveData<AirQualityResponse> = _airQuality

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadDashboardData(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val weatherData = repository.getCurrentWeather(city, apiKey)
                _weather.postValue(weatherData)

                val lat = weatherData.coord.lat
                val lon = weatherData.coord.lon
                val airQualityData = repository.getAirQuality(lat, lon, apiKey)
                _airQuality.postValue(airQualityData)

            } catch (e: Exception) {
                e.printStackTrace()
                _error.postValue("Erro ao carregar dados: ${e.message}")
            }
        }
    }

    class Factory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
