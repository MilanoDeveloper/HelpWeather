package br.com.fiap.helpweather.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.MapViewModel

@SuppressLint("PotentialBehaviorOverride")
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(),
    city: String,
    apiKey: String
) {
    val weather by viewModel.weather.observeAsState()
    val air by viewModel.air.observeAsState()
    val error by viewModel.error.observeAsState()

    LaunchedEffect(city) { viewModel.load(city, apiKey) }

    Column(Modifier.fillMaxSize()) {
        Text("Mapa Ambiental – $city", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
        if (!error.isNullOrBlank()) Text(error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp))
        Box(Modifier.fillMaxSize()) {
            val lat = weather?.coord?.lat ?: -23.55
            val lon = weather?.coord?.lon ?: -46.63
            val aqi = air?.list?.firstOrNull()?.main?.aqi

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        onCreate(Bundle())
                        getMapAsync { gMap ->
                            val pos = LatLng(lat, lon)
                            gMap.uiSettings.isZoomControlsEnabled = true
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10f))
                            gMap.addMarker(
                                MarkerOptions()
                                    .position(pos)
                                    .title("$city")
                                    .snippet("AQI: ${aqi ?: "—"}")
                            )
                        }
                        onResume()
                    }
                }
            )
        }
    }
}
