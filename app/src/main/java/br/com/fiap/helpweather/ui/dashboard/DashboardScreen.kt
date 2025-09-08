package br.com.fiap.helpweather.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val weather by viewModel.weather.observeAsState()
    val airQuality by viewModel.airQuality.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌍 EcoWeather", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        weather?.let {
            Text("Cidade: ${it.name}")
            Text("Temperatura: ${it.main.temp}°C")
            Text("Clima: ${it.weather.firstOrNull()?.description ?: "N/A"}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        airQuality?.list?.firstOrNull()?.let { aqi ->
            Text("Qualidade do Ar (AQI): ${aqi.main.aqi}")
        } ?: Text("Carregando qualidade do ar...")
    }
}
