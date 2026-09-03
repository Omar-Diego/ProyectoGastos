package mx.proyecto.gastos.historial

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.historial.TipoTransaccion
import mx.proyecto.gastos.historial.Transaccion
import mx.proyecto.gastos.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.remove


// --- GENERACIÓN DE DATOS FALSOS ---
/*
fun generarDatosFalsos(): List<Transaccion> {
    return listOf(
        Transaccion(1, "Gasolina", "Transporte", LocalDate.of(2026, 8, 21), 145.00, TipoTransaccion.GASTO, Icons.Default.DirectionsCar, CategoriaTransporte),
        Transaccion(2, "Supermercado", "Comida", LocalDate.of(2026,8,18), 2305.40, TipoTransaccion.GASTO, Icons.Default.ShoppingCart, CategoriaComida),
        Transaccion(3, "Consulta médica", "Salud", LocalDate.of(2026, 8, 16), 450.00, TipoTransaccion.GASTO, Icons.Default.MedicalServices, CategoriaSalud),
        Transaccion(5, "Ropa", "Compras", LocalDate.of(2026, 8, 14), 780.00, TipoTransaccion.GASTO, Icons.Default.Checkroom, CategoriaOcio), // Usando rosa para compras generales
        Transaccion(6, "Salario quincenal", "Salario", LocalDate.of(2026, 8,10), 9500.00, TipoTransaccion.INGRESO, Icons.Default.AttachMoney, CategoriaSalario),
        // Añadimos algunos más para que se pueda hacer scroll
        Transaccion(7, "Restaurante", "Comida", LocalDate.of(2026,8,25), 350.00, TipoTransaccion.GASTO, Icons.Default.Restaurant, CategoriaComida),
        Transaccion(8, "CineDePruebaUeeee", "Entretenimiento", LocalDate.of(2026,8,26), 200.00, TipoTransaccion.GASTO, Icons.Default.Movie, CategoriaOcio),
    ).sortedByDescending { it.fecha } // Ordenar para que el más nuevo esté arriba
}
*/
fun generarDatosFalsos(): List<Movimiento> {
    return listOf(
        Movimiento(
            id = 1L,
            montoCentavos = 14500, // 145.00 convertido a centavos
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.TRANSPORTE,
            fecha = LocalDate.of(2026, 8, 21),
            nota = "Gasolina"
        ),
        Movimiento(
            id = 2L,
            montoCentavos = 230540, // 2305.40 convertido a centavos
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.COMIDA,
            fecha = LocalDate.of(2026, 8, 18),
            nota = "Supermercado"
        ),
        Movimiento(
            id = 3L,
            montoCentavos = 45000,
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.SALUD,
            fecha = LocalDate.of(2026, 8, 16),
            nota = "Consulta médica"
        ),
        Movimiento(
            id = 4L,
            montoCentavos = 78000,
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.OCIO,
            fecha = LocalDate.of(2026, 8, 14),
            nota = "Ropa"
        ),
        Movimiento(
            id = 5L,
            montoCentavos = 950000,
            tipo = TipoMovimiento.INGRESO,
            categoria = Categoria.SALARIO,
            fecha = LocalDate.of(2026, 8, 10),
            nota = "Salario quincenal"
        ),
        Movimiento(
            id = 6L,
            montoCentavos = 35000,
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.COMIDA,
            fecha = LocalDate.of(2026, 8, 25),
            nota = "Restaurante"
        ),
        Movimiento(
            id = 7L,
            montoCentavos = 20000,
            tipo = TipoMovimiento.GASTO,
            categoria = Categoria.OCIO,
            fecha = LocalDate.of(2026, 8, 26),
            nota = "CineDePruebaUeeee"
        )
    ).sortedByDescending { it.fecha }
}

// --- PANTALLA PRINCIPAL ---
@Composable
fun HistorialScreen() {
    // Estado para saber qué filtro está seleccionado
    var filtroSeleccionado by remember { mutableStateOf(FiltroHistorial.TODOS) }

    // Obtenemos los datos falsos y los convertimos a una lista mutable que Compose pueda observar
    val todasLasTransacciones = remember { 
        mutableStateListOf<Movimiento>().apply { addAll(generarDatosFalsos()) }
    }

    // Filtramos la lista según la selección del usuario. 
    // Usamos derivedStateOf para que se recalcule si cambia el filtro o la lista.
    val transaccionesFiltradas by remember(filtroSeleccionado) {
        derivedStateOf {
            when (filtroSeleccionado) {
                FiltroHistorial.TODOS -> todasLasTransacciones
                FiltroHistorial.INGRESOS -> todasLasTransacciones.filter { it.tipo == TipoMovimiento.INGRESO }
                FiltroHistorial.GASTOS -> todasLasTransacciones.filter { it.tipo == TipoMovimiento.GASTO }
            }
        }
    }

    // Agrupar los movimientos por mes (ej. "agosto de 2026")
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

    if (showConfirmation){
        AlertDialog(
            onDismissRequest = { showConfirmation = false }, //Al clickear afuera
            title = { Text("Confirmar acción")},
            text = { Text("¿Estás seguro de que deseas eliminar este movimiento?")},
            confirmButton = { TextButton(onClick = {todasLasTransacciones.remove(transaccionAEliminar)
                showConfirmation = false}) {
                Text("Aceptar")
            }
            },
            dismissButton = {
                TextButton(onClick = {showConfirmation=false}) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        // Aquí es donde integrarías tu barra de navegación inferior existente
        // bottomBar = { TuBarraNavegacionInferior(...) },
        containerColor = Color.White // Fondo blanco como tu boceto
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
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
            if (transaccionesFiltradas.isNotEmpty()){
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
                Column(modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(
                        text = "No hay moviemientos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Empieza agregando movimientos en la pestana \"Registro\"",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }


        }
    }
}