package br.com.fiap.helpweather.ui.actions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.helpweather.viewmodel.ActionsViewModel

@Composable
fun ActionsScreen(
    viewModel: ActionsViewModel = viewModel(),
    city: String,
    apiKey: String
) {
    val actions by viewModel.actions.observeAsState(emptyList())

    LaunchedEffect(city) { viewModel.load(city, apiKey) }

    Column(Modifier.padding(16.dp)) {
        Text("Ações Eco-Friendly", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(actions) { a ->
                ElevatedCard(onClick = { viewModel.toggle(a.id) }) {
                    Row(
                        Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(a.text, Modifier.weight(1f))
                        Checkbox(checked = a.done, onCheckedChange = { viewModel.toggle(a.id) })
                    }
                }
            }
        }
    }
}
