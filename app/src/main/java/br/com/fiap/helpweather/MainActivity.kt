package br.com.fiap.helpweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import br.com.fiap.helpweather.data.api.WeatherApi
import br.com.fiap.helpweather.data.repository.WeatherRepository
import br.com.fiap.helpweather.ui.NavRoute
import br.com.fiap.helpweather.ui.actions.ActionsScreen
import br.com.fiap.helpweather.ui.dashboard.DashboardScreen
import br.com.fiap.helpweather.ui.forecast.ForecastScreen
import br.com.fiap.helpweather.ui.map.MapScreen
import br.com.fiap.helpweather.ui.reports.ReportsScreen
import br.com.fiap.helpweather.ui.theme.HelpWeatherTheme
import br.com.fiap.helpweather.viewmodel.*
import org.osmdroid.config.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val OPENWEATHER_API_KEY = "4bfb39db23c3fa1d24dedeaa5b78f83d"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            val retrofit = remember {
                Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            val api = remember { retrofit.create(WeatherApi::class.java) }
            val weatherRepository = remember { WeatherRepository(api) }

            val navController = rememberNavController()
            var city by rememberSaveable { mutableStateOf("São Paulo") }

            HelpWeatherTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navController.currentBackStackEntryAsState()
                                .value?.destination?.route
                            listOf(
                                NavRoute.Dashboard to Icons.Filled.Home,
                                NavRoute.Forecast to Icons.Filled.Timeline,
                                NavRoute.Map to Icons.Filled.Map,
                                NavRoute.Actions to Icons.Filled.List,
                                NavRoute.Reports to Icons.Filled.Assessment
                            ).forEach { (route, icon) ->
                                NavigationBarItem(
                                    selected = currentRoute == route.route,
                                    onClick = {
                                        navController.navigate(route.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(icon, contentDescription = route.label) },
                                    label = { Text(route.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Dashboard.route,
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                    ) {

                        composable(NavRoute.Dashboard.route) {
                            val vm: DashboardViewModel = viewModel(
                                factory = DashboardViewModel.Factory(weatherRepository)
                            )
                            LaunchedEffect(city) {
                                if (OPENWEATHER_API_KEY.isNotBlank()) {
                                    vm.loadDashboardData(city, OPENWEATHER_API_KEY)
                                }
                            }
                            DashboardScreen(
                                viewModel = vm,
                                defaultCity = city,
                                apiKey = OPENWEATHER_API_KEY,
                                onCityChange = { newCity: String -> city = newCity }
                            )
                        }

                        composable(NavRoute.Forecast.route) {
                            val vm: ForecastViewModel = viewModel(
                                factory = ForecastViewModel.Factory(weatherRepository)
                            )
                            ForecastScreen(
                                viewModel = vm,
                                city = city,
                                apiKey = OPENWEATHER_API_KEY
                            )
                        }

                        composable(NavRoute.Map.route) {
                            val vm: MapViewModel = viewModel(
                                factory = MapViewModel.Factory(weatherRepository)
                            )
                            MapScreen(
                                viewModel = vm,
                                city = city,
                                apiKey = OPENWEATHER_API_KEY
                            )
                        }

                        composable(NavRoute.Actions.route) {
                            val vm: ActionsViewModel = viewModel(
                                factory = ActionsViewModel.Factory(weatherRepository)
                            )
                            ActionsScreen(
                                viewModel = vm,
                                city = city,
                                apiKey = OPENWEATHER_API_KEY
                            )
                        }

                        composable(NavRoute.Reports.route) {
                            val vm: ReportsViewModel = viewModel(
                                factory = ReportsViewModel.Factory(weatherRepository)
                            )
                            ReportsScreen(
                                viewModel = vm,
                                city = city,
                                apiKey = OPENWEATHER_API_KEY
                            )
                        }
                    }
                }
            }
        }
    }
}
