package mx.proyecto.gastos.historial

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.ui.theme.AzulClaro
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.Verde

// Definimos los posibles filtros
enum class FiltroHistorial(
    val titulo: String,
    val icono: ImageVector?,
    val colorFondo: Color,
    val colorContenido: Color,
) {
    TODOS("Todos", Icons.Filled.List, AzulClaro.copy(alpha = 0.2f), AzulClaro),
    INGRESOS("Ingreso", Icons.Filled.AccountBalanceWallet, Verde.copy(alpha = 0.2f), Verde),
    GASTOS("Gasto", Icons.Filled.ShoppingCart, Rojo.copy(alpha = 0.15f), Rojo)
}

@Composable
fun HistorialFiltros(
    filtroSeleccionado: FiltroHistorial,
    onFiltroSelected: (FiltroHistorial) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FiltroHistorial.entries.forEach { filtro ->
            val isSelected = filtro == filtroSeleccionado
            Surface(
                onClick = { onFiltroSelected(filtro) },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 64.dp),
                shape = RoundedCornerShape(12.dp),
                color = filtro.colorFondo,
                border = if (isSelected) BorderStroke(2.dp, filtro.colorContenido)
                         else null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filtro.icono != null) {
                        Icon(
                            imageVector = filtro.icono,
                            contentDescription = null,
                            tint = filtro.colorContenido,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = filtro.titulo,
                        style = MaterialTheme.typography.labelLarge,
                        color = filtro.colorContenido
                    )
                }
            }
        }
    }
}