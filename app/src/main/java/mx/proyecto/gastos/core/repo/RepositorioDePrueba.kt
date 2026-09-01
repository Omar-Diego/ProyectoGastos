package mx.proyecto.gastos.core.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import java.time.LocalDate
import java.time.YearMonth

/**
 * Esta es una clase, los dos puntos ":" quieren decir que se cumple el contrato definido
 * en el anterior archivo que vimos del interface.
 * Por lo tanto esta "obligado" a escribir el cuerpo de las cuatro funciones, cada una con
 * override, que es definir lo que se declaraba unicamente, ya es poner el como por ejemplo.
 *
 * Como se ha visto en los anteriores, se comporta como el repositorio de verdad, pero lo guarda
 * en la RAM. Es unicamente de pruebas, para poder construir y probar las pantallas sin necesidad
 * de la implementacion de Santiago de la Room.
 *
 * La limitacion es que al cerrar la app los datos se van a borrar, cosa que se solventa cuando
 * este integrado Room.
 *
 * QUE ES "MutableStateFlow"
 *   Es un Flow que SIEMPRE tiene un valor actual y ademas se puede modificar
 *   (movimientos.value = ...). Cada vez que le asignas un valor nuevo, se lo emite
 *   a todas las pantallas suscritas y estas se redibujan. Eso es lo que hace que
 *   el resumen y el historial se actualicen solos al registrar o borrar un gasto.
 */
class RepositorioDePrueba : MovimientoRepository {

    // "private" = solo visible dentro de esta clase.
    private var siguienteId = 1L

    // La lista viva de movimientos. Empieza con datos de ejemplo.
    private val movimientos = MutableStateFlow(value = datosDeEjemplo())

    // Devolvemos el MutableStateFlow tal cual: por fuera se ve como un Flow de solo lectura.
    override fun observarTodos(): Flow<List<Movimiento>> = movimientos

    // "map" transforma cada lista emitida en otra ya filtrada por el mes pedido.
    override fun observarMes(mes: YearMonth): Flow<List<Movimiento>> =
        movimientos.map { lista -> lista.filter { YearMonth.from(it.fecha) == mes } }

    override suspend fun guardar(movimiento: Movimiento) {
        movimientos.value =
            if (movimiento.id == 0L) {
                // id 0 = nuevo: le asignamos un id y lo agregamos al final.
                movimientos.value + movimiento.copy(id = siguienteId++)
            } else {
                // ya existe: reemplazamos el que tenga el mismo id.
                movimientos.value.map { if (it.id == movimiento.id) movimiento else it }
            }
    }

    override suspend fun eliminar(movimiento: Movimiento) {
        movimientos.value = movimientos.value.filterNot { it.id == movimiento.id }
    }

    // Datos de arranque para ver las pantallas con contenido.
    private fun datosDeEjemplo(): List<Movimiento> {
        val hoy = LocalDate.now()
        return listOf(
            Movimiento(
                id = siguienteId++,
                montoCentavos = 8_500,
                tipo = TipoMovimiento.GASTO,
                categoria = Categoria.COMIDA,
                fecha = hoy,
                nota = "Cafe"
            ),
            Movimiento(
                id = siguienteId++,
                montoCentavos = 4_500,
                tipo = TipoMovimiento.GASTO,
                categoria = Categoria.TRANSPORTE,
                fecha = hoy.minusDays(1)
            ),
            Movimiento(
                id = siguienteId++,
                montoCentavos = 50_000,
                tipo = TipoMovimiento.INGRESO,
                categoria = Categoria.SALUD,
                fecha = hoy.minusDays(3),
                nota = "Quincena"
            ),
            Movimiento(
                id = siguienteId++,
                montoCentavos = 500_000,
                tipo = TipoMovimiento.INGRESO,
                categoria = Categoria.OCIO,
                fecha = hoy.minusDays(3),
                nota = "Quincena"
            ),
            Movimiento(
                id = siguienteId++,
                montoCentavos = 1_500,
                tipo = TipoMovimiento.INGRESO,
                categoria = Categoria.CASA,
                fecha = hoy.minusDays(3),
                nota = "Quincena"
            ),
            Movimiento(
                id = siguienteId++,
                montoCentavos = 1_000,
                tipo = TipoMovimiento.INGRESO,
                categoria = Categoria.OTRO,
                fecha = hoy.minusDays(3),
                nota = "Quincena"
            ),
        )
    }
}
