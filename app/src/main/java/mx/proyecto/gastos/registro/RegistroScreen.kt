package mx.proyecto.gastos.registro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.proyecto.gastos.core.repo.MovimientoRepository

private enum class Paso { MONTO, CATEGORIA }

@Composable
fun RegistroScreen(
    repositorio: MovimientoRepository,
    alGuardar: () -> Unit,
) {
    val vm: RegistroViewModel = viewModel { RegistroViewModel(repositorio) }
    var paso by rememberSaveable { mutableStateOf(Paso.MONTO) }

    when (paso) {
        Paso.MONTO -> PasoMonto(
            montoCentavos = vm.montoCentavos,
            tipo = vm.tipo,
            montoValido = vm.montoValido,
            alPulsarDigito = vm::pulsarDigito,
            alBorrar = vm::borrar,
            alCambiarTipo = vm::cambiarTipo,
            alContinuar = { paso = Paso.CATEGORIA },
        )
        Paso.CATEGORIA -> PasoCategoria(
            tipo = vm.tipo,
            alElegir = { categoria ->
                vm.guardar(categoria, alTerminar = alGuardar)
            },
            alVolver = { paso = Paso.MONTO },
        )
    }
}