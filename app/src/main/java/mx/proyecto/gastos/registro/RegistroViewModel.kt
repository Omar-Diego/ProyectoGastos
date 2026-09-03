package mx.proyecto.gastos.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.core.repo.MovimientoRepository
import java.time.LocalDate

// Estado y reglas de la pantalla de Registro.

// No DISPLAY

// Recibe el MovimientoRepository del contrato

class RegistroViewModel(
    private val repositorio: MovimientoRepository
): ViewModel(){
    var montoCentavos by mutableLongStateOf(0L)
        private set
    var tipo by mutableStateOf(TipoMovimiento.GASTO)
        private set
    val montoValido: Boolean
        get() = montoCentavos in 1..MONTO_MAXIMO
    fun pulsarDigito(digito: Int){
        val siguiente = montoCentavos * 10 + digito
        if(siguiente <= MONTO_MAXIMO){
            montoCentavos = siguiente
        }
    }

    fun borrar(){
        montoCentavos /= 10
    }

    fun cambiarTipo(nuevo: TipoMovimiento){
        tipo = nuevo
    }

    fun guardar(categoria: Categoria, alTerminar: () -> Unit){
        if(!montoValido) return
        viewModelScope.launch {
            repositorio.guardar(
                Movimiento(
                    montoCentavos = montoCentavos,
                    tipo = tipo,
                    categoria = categoria,
                    fecha = LocalDate.now()
                )
            )
            alTerminar()
        }
    }

    private companion object {
        const val MONTO_MAXIMO = 99_999_999L
    }
}

