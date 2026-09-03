package mx.proyecto.gastos.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.ui.theme.CategoriaCasa
import mx.proyecto.gastos.ui.theme.CategoriaComida
import mx.proyecto.gastos.ui.theme.CategoriaOcio
import mx.proyecto.gastos.ui.theme.CategoriaOtro
import mx.proyecto.gastos.ui.theme.CategoriaSalario
import mx.proyecto.gastos.ui.theme.CategoriaSalud
import mx.proyecto.gastos.ui.theme.CategoriaTransporte
import mx.proyecto.gastos.ui.theme.Rojo
import mx.proyecto.gastos.ui.theme.Verde
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale // Para los meses en español


@Composable
fun TransaccionItem(
    transaccion: Movimiento,
    onDelete: () -> Unit
) {
    //Formato para que la fecha aparezca como "21 ago 2026"
    val formatoFecha = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("es", "ES"))

    //Funcion para determinar color
    val colorIcono = when(transaccion.categoria){
        Categoria.COMIDA -> CategoriaComida
        Categoria.TRANSPORTE -> CategoriaTransporte
        Categoria.CASA -> CategoriaCasa
        Categoria.OCIO -> CategoriaOcio
        Categoria.SALUD -> CategoriaSalud
        Categoria.SALARIO -> CategoriaSalario
        else -> CategoriaOtro
    }

    //Funcion para determinar icono
    val iconoMovimiento = when(transaccion.categoria){
        Categoria.COMIDA -> Icons.Default.Restaurant
        Categoria.TRANSPORTE -> Icons.Default.DirectionsCar
        Categoria.CASA -> Icons.Default.Home
        Categoria.OCIO -> Icons.Default.SportsEsports
        Categoria.SALUD -> Icons.Default.LocalHospital
        Categoria.SALARIO -> Icons.Default.LocalHospital
        else -> Icons.Filled.MoreHoriz
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Espacio entre tarjetas
        shape = RoundedCornerShape(12.dp), // Bordes redondeados como tu dibujo
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0) // Un gris muy claro para el fondo de la tarjeta
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Icono con su color de categoría
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorIcono), // Color de fondo del icono
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoMovimiento,
                    contentDescription = null, // No es necesario para accesibilidad aquí
                    tint = Color.White, // Icono en blanco sobre el fondo de color
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Espacio

            // 2. Textos centrales (Categoría, Descripción, Fecha)
            Column(
                modifier = Modifier.weight(1f) // Ocupa el espacio restante
            ) {
                Text(
                    text = transaccion.categoria.etiqueta,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = transaccion.fecha.format(formatoFecha),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 3. Monto (Derecha) con color según tipo y subrayado
            val montoEnPesos = transaccion.montoCentavos.toDouble() / 100.0

            val montoTexto = if (transaccion.tipo == TipoMovimiento.GASTO) {
                "- \$${"%.2f".format(Locale.US, montoEnPesos)}"
            } else {
                "+ \$${"%.2f".format(Locale.US, montoEnPesos)}"
            }

            val montoColor = if (transaccion.tipo == TipoMovimiento.GASTO) Rojo else Verde

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = montoTexto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = montoColor,
                    // Recreamos el subrayado de tu boceto
                    style = LocalTextStyle.current.copy(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                )
            }
            
            // 4. Botón de borrar
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}