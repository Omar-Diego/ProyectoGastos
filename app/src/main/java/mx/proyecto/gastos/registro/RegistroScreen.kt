package mx.proyecto.gastos.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val isTablet = maxWidth >= 600.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isTablet) Modifier.widthIn(max = 700.dp)
                    else Modifier.fillMaxWidth()
                )
        ) {
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
    }
}
