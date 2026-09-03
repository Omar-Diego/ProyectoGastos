package mx.proyecto.gastos.resumen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import mx.proyecto.gastos.core.repo.MovimientoRepository
import mx.proyecto.gastos.core.repo.RepositorioDePrueba
import mx.proyecto.gastos.ui.components.EmptyState
import mx.proyecto.gastos.ui.theme.AzulClaro
import mx.proyecto.gastos.ui.theme.AzulPrincipal
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.TextColor
import mx.proyecto.gastos.ui.theme.Verde
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale
import kotlin.math.abs

private val MESES_COMPLETOS = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

@Composable
fun ResumenScreen(
    repositorio: MovimientoRepository,
    onIrARegistro: () -> Unit = {},
    viewModel: ResumenViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ResumenViewModel(repositorio) }
        }
    )
) {
    val resumen by viewModel.estado.collectAsStateWithLifecycle()
    ResumenContenido(resumen = resumen)
}

@Composable
private fun ResumenContenido(resumen: ResumenMes) {
    val esVacio = resumen.gastadoMesCentavos == 0L && resumen.ingresosMesCentavos == 0L
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Encabezado()
        
        if (esVacio) {
            EmptyState(
                title = "Sin movimientos aún",
                description = "Registra tu primer ingreso o gasto para ver tu resumen aquí.",
                modifier = Modifier.weight(1f)
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(20.dp))
                TarjetaBalance(resumen)
                Spacer(Modifier.height(20.dp))
                TarjetaHistorial(resumen.historialSeisMeses)
                Spacer(Modifier.height(20.dp))
                TarjetaCategorias(resumen.gastosPorCategoria, resumen.gastadoMesCentavos)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun Encabezado() {
    Column {
        Text(
            text = "Resumen",
            style = MaterialTheme.typography.titleLarge,
            color = TextColor
        )
        Text(
            text = "Aqui tienes el resumen de tus finanzas.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun TarjetaBalance(resumen: ResumenMes) {
    val balanceTotal = resumen.ingresosMesCentavos - resumen.gastadoMesCentavos

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AzulPrincipal)
            .padding(20.dp)
    ) {
        Text(
            text = "Balance total",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatearMoneda(balanceTotal),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )
        val variacion = resumen.variacionBalanceVsMesAnteriorPorcentaje
        if (variacion != null) {
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (variacion >= 0) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (variacion >= 0) Verde else Rojo,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${"%.1f".format(abs(variacion))}%  vs. mes anterior",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AzulClaro),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PillMonto(
                modifier = Modifier.weight(1f),
                etiqueta = "INGRESOS (mes)",
                monto = resumen.ingresosMesCentavos
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(48.dp)
                    .background(Color.White.copy(alpha = 0.3f))
            )
            PillMonto(
                modifier = Modifier.weight(1f),
                etiqueta = "GASTOS (mes)",
                monto = resumen.gastadoMesCentavos
            )
        }
    }
}

@Composable
private fun PillMonto(modifier: Modifier = Modifier, etiqueta: String, monto: Long) {
    Column(
        modifier = modifier.padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatearMoneda(monto),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

@Composable
private fun TarjetaHistorial(historial: List<ResumenMensual>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Ingresos vs gastos",
            style = MaterialTheme.typography.titleMedium,
            color = TextColor
        )
        Text(
            text = "Ultimos 6 meses",
            style = MaterialTheme.typography.bodySmall,
            color = TextColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(16.dp))
        GraficaBarras(historial)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            LeyendaPunto(color = Verde, texto = "INGRESOS")
            LeyendaPunto(color = Rojo, texto = "GASTOS")
        }
    }
}

@Composable
private fun ColumnScope.TarjetaCategorias(
    gastosPorCategoria: List<GastoPorCategoria>,
    gastadoMesCentavos: Long
) {
    val hoy = LocalDate.now()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Gastos por categoria",
            style = MaterialTheme.typography.titleMedium,
            color = TextColor
        )
        Text(
            text = "${MESES_COMPLETOS[hoy.monthValue - 1]} ${hoy.year}",
            style = MaterialTheme.typography.bodySmall,
            color = TextColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(16.dp))
        if (gastosPorCategoria.isEmpty()) {
            Text(
                text = "Sin gastos registrados este mes.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextColor.copy(alpha = 0.5f)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Donut(
                    gastosPorCategoria = gastosPorCategoria,
                    totalCentavos = gastadoMesCentavos
                )
                Spacer(Modifier.width(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    gastosPorCategoria.forEach { item ->
                        LeyendaCategoria(item)
                    }
                }
            }
        }
    }
}


internal fun formatearMoneda(centavos: Long): String {
    val negativo = centavos < 0
    val absCentavos = abs(centavos)
    val pesos = absCentavos / 100
    val centavosRestantes = absCentavos % 100
    val formateador = NumberFormat.getNumberInstance(Locale("es", "MX"))
    val pesosFormateados = formateador.format(pesos)
    val centavosFormateados = centavosRestantes.toString().padStart(2, '0')
    return (if (negativo) "-$" else "$") + "$pesosFormateados.$centavosFormateados"
}