package mx.proyecto.gastos.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.repo.MovimientoRepository

// Estado y reglas de la pantalla de Historial.
//
// Igual que RegistroViewModel, recibe el MovimientoRepository del contrato
// (en main esa implementacion concreta es MovimientoRepositoryRoom), asi la
// pantalla jamas toca ROOM ni la base de datos directamente.

class HistorialViewModel(
    private val repositorio: MovimientoRepository
) : ViewModel() {

    // Observa TODOS los movimientos de la fuente de datos.
    // Room emite una lista NUEVA cada vez que la tabla cambia (al registrar o al
    // eliminar desde cualquier pantalla), y la pantalla se redibuja sola.
    val movimientos: StateFlow<List<Movimiento>> =
        repositorio.observarTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // Eliminar es una accion "de una sola vez" que puede tardar (disco), por eso
    // suspend dentro del contrato y la lanzamos en el viewModelScope.
    fun eliminar(movimiento: Movimiento) {
        viewModelScope.launch {
            repositorio.eliminar(movimiento)
        }
    }
}
