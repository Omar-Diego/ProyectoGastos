package mx.proyecto.gastos.historial

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

// Tipos de transacciones para saber si es ingreso o gasto
enum class TipoTransaccion {
    INGRESO, GASTO
}

// Estructura de cada movimiento
data class Transaccion(
    val id: Int,
    val categoria: String,    // Ej: "Gasolina", "Supermercado"
    val descripcion: String,  // Ej: "Transporte", "Comida"
    val fecha: LocalDate,        // Ej: "21-ago"
    val monto: Double,       // Ej: 145.00
    val tipo: TipoTransaccion,
    val icon: ImageVector,    // El icono que se mostrará
    val iconColor: Color      // El color del icono según tu boceto
)