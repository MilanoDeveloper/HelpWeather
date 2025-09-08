package br.com.fiap.helpweather.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.fiap.helpweather.viewmodel.DashboardViewModel
import br.com.fiap.helpweather.util.aqiDescription
import br.com.fiap.helpweather.ui.components.AqiPill
import br.com.fiap.helpweather.ui.components.SectionCard
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import br.com.fiap.helpweather.R

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    defaultCity: String = "São Paulo",
    apiKey: String = "",
    onCityChange: (String) -> Unit = {}
) {

    val weather by viewModel.weather.observeAsState(initial = null)
    val airQuality by viewModel.airQuality.observeAsState(initial = null)
    val errorMsg by viewModel.error.observeAsState(initial = null)

    var city by rememberSaveable { mutableStateOf(defaultCity) }
    val focus = androidx.compose.ui.platform.LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_helpweather),
            contentDescription = "Logo HelpWeather",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
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
                        if (c.isNotEmpty() && apiKey.isNotEmpty()) {
                            viewModel.loadDashboardData(c, apiKey)
                            onCityChange(c)
                            focus.clearFocus()
                        }
                    }
                ),
                leadingIcon = { Icon(Icons.Default.Public, contentDescription = null) }
            )
            Button(onClick = {
                val c = city.trim()
                if (c.isNotEmpty() && apiKey.isNotEmpty()) {
                    viewModel.loadDashboardData(c, apiKey)
                    onCityChange(c)
                    focus.clearFocus()
                }
            }) { Text("Buscar") }
        }

        SectionCard(title = "Clima agora", icon = {
            val icon = weather?.weather?.firstOrNull()?.icon
            if (!icon.isNullOrBlank()) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${icon}@2x.png",
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }) {
            if (weather == null) {
                CircularProgressIndicator()
            } else {
                Text("Cidade: ${weather!!.name}")
                Spacer(Modifier.height(6.dp))
                Text("Temperatura: ${"%.1f".format(weather!!.main.temp)}°C • ${weather!!.weather.firstOrNull()?.description ?: "—"}")
                Spacer(Modifier.height(6.dp))
                Text("Umidade: ${weather!!.main.humidity}%   •   Vento: ${weather!!.wind.speed} m/s")
            }
        }

        SectionCard(title = "Qualidade do ar") {
            val aqiValue = airQuality?.list?.firstOrNull()?.main?.aqi
            if (aqiValue == null) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val desc = aqiDescription(aqiValue)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AqiPill(aqi = aqiValue, label = desc)
                    Text("Nível: $desc")
                }
            }
        }
    }
}