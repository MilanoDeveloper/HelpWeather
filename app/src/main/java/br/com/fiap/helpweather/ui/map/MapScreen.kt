package br.com.fiap.helpweather.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.MapViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.viewinterop.AndroidView

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
        Text(
            "Mapa (OpenStreetMap) – $city",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (!error.isNullOrBlank()) {
            Text(
                error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        val lat = weather?.coord?.lat
        val lon = weather?.coord?.lon
        val aqi = air?.list?.firstOrNull()?.main?.aqi

        if (lat == null || lon == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val context = LocalContext.current
            val mapView = remember {
                MapView(context).apply {
                    id = android.R.id.content
                    setTileSource(TileSourceFactory.MAPNIK)
                    controller.setZoom(11.0)
                }
            }

            DisposableEffect(Unit) {
                mapView.onResume()
                onDispose {
                    mapView.onPause()
                    mapView.onDetach()
                }
            }

            LaunchedEffect(lat, lon, aqi) {
                val point = GeoPoint(lat, lon)
                mapView.controller.setCenter(point)
                mapView.overlays.clear()
                val marker = Marker(mapView).apply {
                    position = point
                    title = city
                    snippet = "AQI: ${aqi ?: "—"}"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                mapView.overlays.add(marker)
                mapView.invalidate()
            }

            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
