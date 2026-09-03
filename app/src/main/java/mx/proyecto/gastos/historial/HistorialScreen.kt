package mx.proyecto.gastos.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.core.repo.MovimientoRepository
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
            onDismissRequest = { showConfirmation = false }, //Al clickear afuera
            title = { Text("Confirmar acción") },
            text = { Text("¿Estás seguro de que deseas eliminar este movimiento?") },
            confirmButton = {
                TextButton(onClick = {
                    // Eliminar de verdad: se borra de la base de datos a traves del repositorio.
                    transaccionAEliminar?.let { vm.eliminar(it) }
                    showConfirmation = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // La barra de navegacion inferior NO se dibuja aqui: vive en NavGraph.kt (App),
    // que ya envuelve esta pantalla con su Scaffold y su bottomBar. Aqui solo se pinta
    // el contenido de la pantalla sobre el fondo blanco del boceto.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Cabecera (Header)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Historial",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.History, // Un icono de calendario/historial
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Todos tus movimientos en un lugar.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        HistorialFiltros(
            filtroSeleccionado = filtroSeleccionado
        ) { nuevoFiltro -> filtroSeleccionado = nuevoFiltro }

        Spacer(modifier = Modifier.height(16.dp))

        //Si la lista no esta vacia
        if (transaccionesFiltradas.isNotEmpty()) {
            // 4. Lista de Transacciones (LazyColumn es como un RecyclerView)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ocupa el espacio restante hasta la barra inferior
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                // Recorremos cada grupo (mes y su lista de movimientos)
                transaccionesAgrupadas.forEach { (mes, movimientosDelMes) ->
                    // 1. El encabezado con el mes
                    item(key = mes) {
                        Text(
                            text = mes.replaceFirstChar { it.uppercase() },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }

                    // 2. Las transacciones de ese mes
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No hay movimientos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Empieza agregando movimientos en la pestaña \"Registro\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
