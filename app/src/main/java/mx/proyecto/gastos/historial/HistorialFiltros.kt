package mx.proyecto.gastos.historial

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.ui.theme.AzulClaro

// Definimos los posibles filtros
enum class FiltroHistorial(val titulo: String) {
    TODOS("Todos"),
    INGRESOS("Ingresos"),
    GASTOS("Gastos")
}

@OptIn(ExperimentalMaterial3Api::class) // Necesario para FilterChip
@Composable
fun HistorialFiltros(
    filtroSeleccionado: FiltroHistorial,
    onFiltroSelected: (FiltroHistorial) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        FiltroHistorial.entries.forEach { filtro ->
            val isSelected = filtro == filtroSeleccionado
            FilterChip(
                selected = isSelected,
                onClick = { onFiltroSelected(filtro) },
                label = { Text(filtro.titulo) },
                modifier = Modifier.padding(end = 8.dp),
                // Usamos el color AzulPrimario para el chip seleccionado
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AzulClaro.copy(alpha = 0.1f), // Un azul muy suave
                    selectedLabelColor = AzulClaro,
                    selectedLeadingIconColor = AzulClaro
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline,
                    selectedBorderColor = AzulClaro
                )
            )
        }
    }
}