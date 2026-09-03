package mx.proyecto.gastos.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mx.proyecto.gastos.core.repo.MovimientoRepository
import java.time.LocalDate

class ResumenViewModel(
    private val repositorio: MovimientoRepository,
    private val hoy: LocalDate = LocalDate.now()
) : ViewModel() {

    val estado: StateFlow<ResumenMes> =
        repositorio.observarTodos()
            .map { movimientos -> CalculadoraResumen.calcular(movimientos, hoy) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ResumenMes.VACIO
            )
}