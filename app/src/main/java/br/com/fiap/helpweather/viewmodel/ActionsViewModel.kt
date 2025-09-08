package br.com.fiap.helpweather.viewmodel

import androidx.lifecycle.*
import br.com.fiap.helpweather.data.model.WeatherResponse
import br.com.fiap.helpweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

data class EcoAction(val id: String, val text: String, val done: Boolean)

class ActionsViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _actions = MutableLiveData<List<EcoAction>>(emptyList())
    val actions: LiveData<List<EcoAction>> = _actions

    fun load(city: String, apiKey: String) {
        viewModelScope.launch {
            val w = repo.getCurrentWeather(city, apiKey)
            _weather.postValue(w)
            _actions.postValue(generateActions(w))
        }
    }

    fun toggle(id: String) {
        _actions.value = _actions.value?.map { if (it.id == id) it.copy(done = !it.done) else it }
    }

    private fun generateActions(w: WeatherResponse): List<EcoAction> {
        val tips = mutableListOf<String>()
        val desc = w.weather.firstOrNull()?.main ?: ""
        if (w.main.temp in 18.0..27.0) tips += "Vá a pé ou de bicicleta (clima favorável)."
        if (desc.contains("Rain", true)) tips += "Colete água de chuva para regar plantas."
        if (w.main.temp >= 30) tips += "Use ventilador em vez de ar-condicionado."
        if (w.wind.speed >= 5) tips += "Aproveite ventilação natural, abra janelas."
        if (tips.isEmpty()) tips += "Desligue equipamentos em stand-by à noite."

        val today = LocalDate.now().toString()
        return tips.mapIndexed { i, t -> EcoAction("$today-$i", t, false) }
    }

    class Factory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(c: Class<T>): T {
            if (c.isAssignableFrom(ActionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return ActionsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM")
        }
    }
}
