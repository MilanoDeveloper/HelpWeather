package br.com.fiap.helpweather.ui.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.ForecastViewModel

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = viewModel(),
    city: String,
    apiKey: String
) {
    val forecast by viewModel.forecast.observeAsState()
    val error by viewModel.error.observeAsState()

    LaunchedEffect(city) { viewModel.load(city, apiKey) }

    Column(Modifier.padding(16.dp)) {
        Text("Previsão – $city", style = MaterialTheme.typography.titleMedium)
        if (!error.isNullOrBlank()) Text(error!!, color = MaterialTheme.colorScheme.error)

        if (forecast == null) {
            CircularProgressIndicator()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(forecast!!.list) { item ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp)) {
                            Text(item.dt_txt)
                            Text("Temp: ${item.main.temp}°C | Sensação: ${item.main.feels_like}°C")
                            Text("Clima: ${item.weather.firstOrNull()?.description ?: "—"}")
                            Text("Vento: ${item.wind.speed} m/s")
                            Text(sustTip(item.main.temp, item.weather.firstOrNull()?.main))
                        }
                    }
                }
            }
        }
    }
}

private fun sustTip(temp: Double, main: String?): String = when {
    (main ?: "").contains("Rain", true) -> "Dica: Aproveite para coletar água de chuva."
    temp >= 30 -> "Dica: Prefira ventilador ao ar-condicionado hoje."
    temp in 18.0..27.0 -> "Dica: Dia bom para caminhar / transporte ativo."
    else -> "Dica: Economize energia, desligue equipamentos em stand-by."
}
