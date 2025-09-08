package br.com.fiap.helpweather.viewmodel

import androidx.lifecycle.*
import br.com.fiap.helpweather.data.model.ForecastResponse
import br.com.fiap.helpweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class ForecastViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _forecast = MutableLiveData<ForecastResponse>()
    val forecast: LiveData<ForecastResponse> = _forecast

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun load(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _forecast.postValue(repo.getForecast(city, apiKey))
                _error.postValue(null)
            } catch (e: Exception) {
                _error.postValue("Falha ao buscar previsão: ${e.message}")
            }
        }
    }

    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(ForecastViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ForecastViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM")
        }
    }
}
