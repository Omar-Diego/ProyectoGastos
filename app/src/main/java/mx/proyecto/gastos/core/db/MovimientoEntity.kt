package mx.proyecto.gastos.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import java.time.LocalDate

@Entity(tableName = "movimientos") //Nombre real de la tabla en SQLite
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val montoCentavos: Long,
    val tipo: TipoMovimiento,
    val categoria: Categoria,
    val fecha: LocalDate,
    val nota: String? = null
)