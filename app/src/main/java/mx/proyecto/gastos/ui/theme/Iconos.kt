package mx.proyecto.gastos.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import mx.proyecto.gastos.core.modelo.Categoria

/**
 * El icono de cada categoria, definido en UN solo lugar para que Registro e
 * Historial muestren siempre el mismo simbolo. (Igual que [Categoria.color].)
 */
val Categoria.icono: ImageVector
    get() = when (this) {
        Categoria.COMIDA -> Icons.Filled.Restaurant
        Categoria.TRANSPORTE -> Icons.Filled.DirectionsCar
        Categoria.CASA -> Icons.Filled.Home
        Categoria.OCIO -> Icons.Filled.SportsEsports
        Categoria.SALUD -> Icons.Filled.LocalHospital
        Categoria.SALARIO -> Icons.Filled.AccountBalance
        Categoria.OTRO -> Icons.Filled.MoreHoriz
    }
