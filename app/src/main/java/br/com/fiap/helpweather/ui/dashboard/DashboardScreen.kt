package br.com.fiap.helpweather.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.helpweather.viewmodel.DashboardViewModel


import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import br.com.fiap.helpweather.util.aqiDescription

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    defaultCity: String = "São Paulo",
    apiKey: String,
    onCityChange: (String) -> Unit = {}
) {
    val weather by viewModel.weather.observeAsState()
    val airQuality by viewModel.airQuality.observeAsState()
    val focus = LocalFocusManager.current

    var city by rememberSaveable { mutableStateOf(defaultCity) }

    LaunchedEffect(defaultCity) {
        city = defaultCity
        viewModel.loadDashboardData(defaultCity, apiKey)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🌍 EcoWeather", style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Cidade") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        val c = city.trim()
                        if (c.isNotEmpty()) {
                            viewModel.loadDashboardData(c, apiKey)
                            onCityChange(c)
                            focus.clearFocus()
                        }
                    }
                )
            )

            Button(
                onClick = {
                    val c = city.trim()
                    if (c.isNotEmpty()) {
                        viewModel.loadDashboardData(c, apiKey)
                        onCityChange(c)
                        focus.clearFocus()
                    }
                }
            ) { Text("Buscar") }
        }

        if (weather != null) {
            Text("Cidade: ${weather!!.name}")
            Text("Temperatura: ${"%.1f".format(weather!!.main.temp)}°C")
            Text("Clima: ${weather!!.weather.firstOrNull()?.description ?: "N/A"}")
        } else {
            CircularProgressIndicator()
        }

        airQuality?.list?.firstOrNull()?.let { aqi ->
            Text("Qualidade do Ar: ${aqiDescription(aqi.main.aqi)}")
        } ?: Text("Carregando qualidade do ar...")
    }
}
