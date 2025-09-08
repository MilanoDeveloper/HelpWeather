// br.com.fiap.ecoweather.ui.reports.ReportsScreen
package br.com.fiap.helpweather.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.ReportsViewModel

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = viewModel(),
    city: String,
    apiKey: String,
    completedFromActions: Int = 0
) {
    val report by viewModel.report.observeAsState()

    LaunchedEffect(city, completedFromActions) {
        viewModel.markCompletedFromActions(completedFromActions)
        viewModel.load(city, apiKey)
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Relatórios", style = MaterialTheme.typography.titleMedium)
        if (report == null) {
            CircularProgressIndicator()
        } else {
            ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ações eco concluídas: ${report!!.completedActions}")
                    Text("Média de temperatura prevista: ${report!!.forecastAvgTemp ?: "—"} °C")
                }
            }
        }
    }
}
