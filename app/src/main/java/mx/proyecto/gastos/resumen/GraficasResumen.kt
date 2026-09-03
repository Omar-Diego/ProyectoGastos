package mx.proyecto.gastos.resumen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.TextColor
import mx.proyecto.gastos.ui.theme.Verde
import mx.proyecto.gastos.ui.theme.color
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

private val MESES_ABREVIADOS = listOf(
    "ENE", "FEB", "MAR", "ABR", "MAY", "JUN",
    "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"
)

@Composable
internal fun LeyendaPunto(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            color = TextColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
internal fun GraficaBarras(historial: List<ResumenMensual>) {
    val maximoReal = historial
        .flatMap { listOf(it.ingresosCentavos, it.gastosCentavos) }
        .maxOrNull()
        ?.takeIf { it > 0L } ?: 100_00L

    val techoEje = techoLimpio(maximoReal)
    val etiquetasEje = listOf(techoEje, techoEje * 3 / 4, techoEje / 2, techoEje / 4, 0L)

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .height(140.dp)
                .padding(end = 6.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            etiquetasEje.forEach { valor ->
                Text(
                    text = formatearEje(valor),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextColor.copy(alpha = 0.4f)
                )
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(140.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            historial.forEach { mesResumen ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Barra(
                            fraccion = mesResumen.ingresosCentavos.toFloat() / techoEje.toFloat(),
                            color = Verde
                        )
                        Spacer(Modifier.width(4.dp))
                        Barra(
                            fraccion = mesResumen.gastosCentavos.toFloat() / techoEje.toFloat(),
                            color = Rojo
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = MESES_ABREVIADOS[mesResumen.mes.monthValue - 1],
                        style = MaterialTheme.typography.bodySmall,
                        color = TextColor.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Barra(fraccion: Float, color: Color) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight(fraccion.coerceIn(0.01f, 1f))
            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .background(color)
    )
}

private fun techoLimpio(maximo: Long): Long {
    if (maximo <= 0L) return 100_00L
    val magnitud = 10.0.pow(ceil(log10(maximo.toDouble())) - 1).toLong().coerceAtLeast(1L)
    return ceil(maximo.toDouble() / magnitud).toLong() * magnitud
}

private fun formatearEje(centavos: Long): String {
    val pesos = centavos / 100
    return when {
        pesos == 0L -> "0"
        pesos >= 1_000 -> "${pesos / 1_000}k"
        else -> pesos.toString()
    }
}

@Composable
internal fun Donut(gastosPorCategoria: List<GastoPorCategoria>, totalCentavos: Long) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val grosor = 22.dp.toPx()
            var anguloInicial = -90f
            gastosPorCategoria.forEach { item ->
                val barrido = (item.porcentaje / 100.0 * 360.0).toFloat()
                drawArc(
                    color = item.categoria.color,
                    startAngle = anguloInicial,
                    sweepAngle = barrido,
                    useCenter = false,
                    style = Stroke(width = grosor, cap = StrokeCap.Butt),
                    size = Size(size.width - grosor, size.height - grosor),
                    topLeft = Offset(grosor / 2, grosor / 2)
                )
                anguloInicial += barrido
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TOTAL:",
                style = MaterialTheme.typography.labelMedium,
                color = TextColor.copy(alpha = 0.5f)
            )
            Text(
                text = formatearMoneda(totalCentavos),
                style = MaterialTheme.typography.titleSmall,
                color = TextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
internal fun LeyendaCategoria(item: GastoPorCategoria) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(item.categoria.color)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = nombreCategoria(item.categoria),
            style = MaterialTheme.typography.bodyMedium,
            color = TextColor.copy(alpha = 0.8f),
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = "${item.porcentaje.roundToInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            color = TextColor
        )
    }
}

private fun nombreCategoria(categoria: Categoria): String =
    categoria.name.lowercase().replaceFirstChar { it.uppercase() }

private fun Double.roundToInt(): Int = round(this).toInt()