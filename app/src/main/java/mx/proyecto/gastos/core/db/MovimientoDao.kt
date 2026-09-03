package mx.proyecto.gastos.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MovimientoDao {

    // Todos los movimientos entre dos fechas (inclusive), ordenados del más reciente al más viejo
    @Query("SELECT * FROM movimientos WHERE fecha BETWEEN :inicio AND :fin ORDER BY fecha DESC")
    fun observarEntreFechas(inicio: LocalDate, fin: LocalDate): Flow<List<MovimientoEntity>>

    // Todos los movimientos sin filtrar
    @Query("SELECT * FROM movimientos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<MovimientoEntity>>

    // Inserta si es nuevo (id = 0), actualiza si ya existe (id != 0)
    @Upsert
    suspend fun guardar(movimiento: MovimientoEntity)

    @Delete
    suspend fun eliminar(movimiento: MovimientoEntity)
}