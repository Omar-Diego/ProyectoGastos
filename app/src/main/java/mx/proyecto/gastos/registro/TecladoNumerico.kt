package mx.proyecto.gastos.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.ui.theme.AzulClaro

/**
 * Teclado numerico propio: rejilla 3x4. No usa el teclado del sistema.
 *   1 2 3
 *   4 5 6
 *   7 8 9
 *     0 ⌫
 */
@Composable
fun TecladoNumerico(
    alPulsarDigito: (Int) -> Unit,
    alBorrar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val filas = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "⌫"),
    )
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        filas.forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fila.forEach { simbolo ->
                    when (simbolo) {
                        ""  -> Box(Modifier.weight(1f))
                        "⌫" -> Tecla("⌫", "Borrar un dígito", Modifier.weight(1f), alBorrar)
                        else -> Tecla(simbolo, null, Modifier.weight(1f)) {
                            alPulsarDigito(simbolo.toInt())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Tecla(
    texto: String,
    descripcion: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = 56.dp)
            .then(
                if (descripcion != null) Modifier.semantics { contentDescription = descripcion }
                else Modifier
            ),
        shape = MaterialTheme.shapes.large,
        color = AzulClaro.copy(alpha = 0.15f),
    ) {
        Box(Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text(texto, style = MaterialTheme.typography.titleLarge)
        }
    }
}
