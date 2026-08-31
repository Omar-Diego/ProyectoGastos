package mx.proyecto.gastos.core.repo

import kotlinx.coroutines.flow.Flow
import mx.proyecto.gastos.core.modelo.Movimiento
import java.time.YearMonth

/**
 * Nuevamente para referencia, la interface es como una funcion, lista de operaciones
 * que se deben cumplir, aqui solo decimos lo que existe, no como se guarda ni donde.
 *
 * Esto para que los modulos (registro, resumen, historial), reciban un "MovimientoRepository"
 * y llaman a estas funciones; sin nombrear una base de datos concreta. Es justo perfecto
 * para probar desde el inicio para no depender del modelo de datos de la nube o ROOM, etc.
 */
interface MovimientoRepository {

    /**
     * En estos documentos iniciales documento de mas e innecesario para los demas documentos, pero
     * debido a que todos usaremos estos archivos mejor que quede claro.
     *
     * El Flow<List<Movimiento>> como lo dice el nombre es como lo que permite
     * entregar una lista NUEVA cada vez que cambian los datos.
     *
     * Permite volver a dibujar sola, por eso si el resumen cambiara al registrar un gasto.
     * Devuelve los movimientos cuya fecha cae en [mes]. La usan resumen e historial.
     */
    fun observarMes(mes: YearMonth): Flow<List<Movimiento>>

    /**
     * Igual que [observarMes] pero SIN filtrar por fecha: todos los movimientos.
     * Es la base de la busqueda y los filtros de la segunda entrega.
     */
    fun observarTodos(): Flow<List<Movimiento>>

    /**
     * Si observamos las siguientes funciones poseen suspend, que marca a una funcion
     * que puede PAUSARSE mientras hace algo lento, como escribir en disco o las llamadas a la nube y
     * CONTINUAR despues, esto evita CONGELAR la pantalla.
     *
     * La pregunta ahora es por que lo hacemos asi:
     *   guardar y eliminar son acciones de "una sola vez" que pueden tardar.
     *   observar* entregan un Flow de inmediato, no tardan.
     *
     * Si el movimiento trae id = 0 lo inserta; si trae id, actualiza el existente.
     */
    suspend fun guardar(movimiento: Movimiento)
    suspend fun eliminar(movimiento: Movimiento)
}
