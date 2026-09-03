package mx.proyecto.gastos.core.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.proyecto.gastos.core.db.MovimientoDao
import mx.proyecto.gastos.core.db.toDominio
import mx.proyecto.gastos.core.db.toEntity
import mx.proyecto.gastos.core.modelo.Movimiento
import java.time.YearMonth

class MovimientoRepositoryRoom(
    private val dao: MovimientoDao
) : MovimientoRepository {

    override fun observarMes(mes: YearMonth): Flow<List<Movimiento>> {
        val inicio = mes.atDay(1)
        val fin = mes.atEndOfMonth()
        return dao.observarEntreFechas(inicio, fin).map { lista ->
            lista.map { it.toDominio() }
        }
    }

    override fun observarTodos(): Flow<List<Movimiento>> {
        return dao.observarTodos().map { lista ->
            lista.map { it.toDominio() }
        }
    }

    override suspend fun guardar(movimiento: Movimiento) {
        dao.guardar(movimiento.toEntity())
    }

    override suspend fun eliminar(movimiento: Movimiento) {
        dao.eliminar(movimiento.toEntity())
    }
}