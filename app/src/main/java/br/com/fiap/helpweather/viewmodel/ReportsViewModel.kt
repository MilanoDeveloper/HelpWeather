package br.com.fiap.helpweather.viewmodel

import androidx.lifecycle.*
import br.com.fiap.helpweather.data.model.ForecastResponse
import br.com.fiap.helpweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class ReportData(
    val completedActions: Int,
    val forecastAvgTemp: Int?
)

class ReportsViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _report = MutableLiveData<ReportData>()
    val report: LiveData<ReportData> = _report
    private var completed = 0

    fun markCompletedFromActions(count: Int) {
        completed = count
        _report.value = _report.value?.copy(completedActions = count)
    }

    fun load(city: String, apiKey: String) {
        viewModelScope.launch {
            val fc: ForecastResponse = repo.getForecast(city, apiKey)
            val avg = fc.list.takeIf { it.isNotEmpty() }?.map { it.main.temp }?.average()
            _report.postValue(ReportData(completed, avg?.roundToInt()))
        }
    }

    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(ReportsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ReportsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM")
        }
    }
}
