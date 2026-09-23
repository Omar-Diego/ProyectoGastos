package mx.proyecto.gastos.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.core.repo.MovimientoRepository
import mx.proyecto.gastos.ui.components.EmptyState
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.Verde
import java.time.format.DateTimeFormatter
import java.util.Locale

// --- PANTALLA PRINCIPAL ---
@Composable
fun HistorialScreen(repositorio: MovimientoRepository) {
    // La pantalla no crea datos: observa los movimientos guardados en la fuente de
    // datos real (en main es ROOM via MovimientoRepositoryRoom). Al registrar o
    // eliminar un movimiento, la lista nueva llega sola y Compose se redibuja.
    val vm: HistorialViewModel = viewModel { HistorialViewModel(repositorio) }
    val todasLasTransacciones by vm.movimientos.collectAsState(initial = emptyList())

    // Estado para saber qué filtro está seleccionado
    var filtroSeleccionado by remember { mutableStateOf(FiltroHistorial.TODOS) }

    // Filtramos la lista según la selección del usuario
    val transaccionesFiltradas = when (filtroSeleccionado) {
        FiltroHistorial.TODOS -> todasLasTransacciones
        FiltroHistorial.INGRESOS -> todasLasTransacciones.filter { it.tipo == TipoMovimiento.INGRESO }
        FiltroHistorial.GASTOS -> todasLasTransacciones.filter { it.tipo == TipoMovimiento.GASTO }
    }

    // Agrupar los movimientos por mes (ej. "agosto 2026").
    // La lista ya llega ordenada de la más reciente a la más vieja (la query de Room
    // hace ORDER BY fecha DESC), así que solo agrupamos.
    val transaccionesAgrupadas = remember(transaccionesFiltradas) {
        transaccionesFiltradas
            .groupBy {
                it.fecha.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES")))
            }
            .entries
            .sortedByDescending { entry -> entry.value.maxOf { it.fecha } }
    }

    // -- PANTALLA DE CONFIRMACION DE ELIMINACION --
    var showConfirmation by remember { mutableStateOf(false) }
    var transaccionAEliminar by remember { mutableStateOf<Movimiento?>(null) }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            containerColor = Color.White,
            titleContentColor = mx.proyecto.gastos.ui.theme.AzulPrincipal,
            textContentColor = Color.Black,
            confirmButton = {
                TextButton(onClick = {
                    transaccionAEliminar?.let { vm.eliminar(it) }
                    showConfirmation = false
                }) {
                    Text("Aceptar", color = mx.proyecto.gastos.ui.theme.AzulPrincipal)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancelar", color = mx.proyecto.gastos.ui.theme.AzulPrincipal)
                }
            },
            title = { Text("Confirmar acción") },
            text = { Text("¿Estás seguro de que deseas eliminar este movimiento?") }
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        val isTablet = maxWidth >= 600.dp
        /*BoxWithConstraints mide automáticamente el espacio disponible (maxWidth).
•       Si la pantalla es una tablet o pantalla ancha (maxWidth >= 600.dp), se le aplica un ancho máximo de 520.dp y se centra.
        Si es un teléfono, ocupa todo el ancho (fillMaxWidth).*/

        //Responsive (La barra de navegacion vive en otra parte)
        Column(
            modifier = Modifier
                .fillMaxSize()
                // En teléfonos usa el 100%, en tablets se limita a 520dp máximo y se centra
                .then(
                    if (isTablet) Modifier.widthIn(max = 520.dp)
                    else Modifier.fillMaxWidth()
                )
        ) {
            // 1. Cabecera (Header)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Text(
                    text = "Todos tus movimientos en un lugar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (todasLasTransacciones.isNotEmpty()) {
                HistorialFiltros(
                    filtroSeleccionado = filtroSeleccionado
                ) { nuevoFiltro -> filtroSeleccionado = nuevoFiltro }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (transaccionesFiltradas.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    transaccionesAgrupadas.forEach { (mes, movimientosDelMes) ->
                        item(key = mes) {
                            Text(
                                text = mes.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                        }

                        items(movimientosDelMes, key = { it.id }) { movimiento ->
                            TransaccionItem(
                                transaccion = movimiento,
                                onDelete = {
                                    transaccionAEliminar = movimiento
                                    showConfirmation = true
                                }
                            )
                        }
                    }
                }
            } else {
                val (titulo, descripcion) = when {
                    todasLasTransacciones.isEmpty() -> Pair(
                        "Sin movimientos aún",
                        "Registra tu primer ingreso o gasto para ver tu historial aquí."
                    )
                    filtroSeleccionado == FiltroHistorial.INGRESOS -> Pair(
                        "Sin ingresos",
                        "Aún no tienes ingresos registrados. Tus ingresos aparecerán aquí."
                    )
                    filtroSeleccionado == FiltroHistorial.GASTOS -> Pair(
                        "Sin gastos",
                        "Aún no tienes gastos registrados. Tus gastos aparecerán aquí."
                    )
                    else -> Pair(
                        "Sin movimientos",
                        "No se encontraron movimientos con el filtro seleccionado."
                    )
                }
                EmptyState(
                    title = titulo,
                    description = descripcion,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
