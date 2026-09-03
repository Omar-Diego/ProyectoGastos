package mx.proyecto.gastos.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.ui.theme.AzulPrincipal
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.Verde
import mx.proyecto.gastos.ui.theme.color

/**
 * Paso 1: captura del monto con teclado propio y selector gasto/ingreso (RN-3).
 */
@Composable
fun PasoMonto(
    montoCentavos: Long,
    tipo: TipoMovimiento,
    montoValido: Boolean,
    alPulsarDigito: (Int) -> Unit,
    alBorrar: () -> Unit,
    alCambiarTipo: (TipoMovimiento) -> Unit,
    alContinuar: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(16.dp).padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // RN-3: selector gasto / ingreso — ancho completo con iconos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Gasto: fondo rosa claro + icono/texto rojo oscuro
            SelectorTipo(
                texto = "Gasto",
                icono = Icons.Filled.ShoppingCart,
                colorFondo = Rojo.copy(alpha = 0.15f),
                colorContenido = Rojo,
                seleccionado = tipo == TipoMovimiento.GASTO,
                onClick = { alCambiarTipo(TipoMovimiento.GASTO) },
                modifier = Modifier.weight(1f),
            )
            // Ingreso: fondo verde claro + icono/texto verde cuando seleccionado
            SelectorTipo(
                texto = "Ingreso",
                icono = Icons.Filled.AccountBalanceWallet,
                colorFondo = Verde.copy(alpha = 0.2f),
                colorContenido = Verde,
                seleccionado = tipo == TipoMovimiento.INGRESO,
                onClick = { alCambiarTipo(TipoMovimiento.INGRESO) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.weight(1f))

        // Monto grande — azul oscuro ( Navy )
        Text(
            text = montoCentavos.pesos(),
            style = MaterialTheme.typography.displayLarge,
            color = if (montoValido) Color(0xFF0D1B4A)
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
        )

        Spacer(Modifier.weight(1f))

        TecladoNumerico(alPulsarDigito = alPulsarDigito, alBorrar = alBorrar)

        Spacer(Modifier.height(16.dp))

        // RN-2: en $0.00 el boton queda deshabilitado
        Button(
            onClick = alContinuar,
            enabled = montoValido,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0D1B4A),
                disabledContainerColor = Color(0xFF0D1B4A).copy(alpha = 0.4f),
            ),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}

@Composable
private fun SelectorTipo(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorFondo: Color,
    colorContenido: Color,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 64.dp),
        shape = RoundedCornerShape(12.dp),
        color = colorFondo,
        border = if (seleccionado) BorderStroke(2.dp, colorContenido)
                 else null,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorContenido,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = colorContenido,
            )
        }
    }
}
