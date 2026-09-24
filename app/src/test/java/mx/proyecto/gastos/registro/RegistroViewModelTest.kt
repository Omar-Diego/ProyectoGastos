package mx.proyecto.gastos.registro

import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.core.repo.RepositorioDePrueba
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistroViewModelTest {

    // MONTO_MAXIMO en RegistroViewModel: 99_999_999 centavos = $999,999.99
    private val montoMaximoCentavos = 99_999_999L

    private fun viewModel() = RegistroViewModel(RepositorioDePrueba())

    @Test
    fun `monto en cero no es valido`() {
        val vm = viewModel()

        assertFalse(vm.montoValido)
    }

    @Test
    fun `monto mayor a cero es valido`() {
        val vm = viewModel()

        vm.pulsarDigito(5)

        assertTrue(vm.montoValido)
        assertEquals(5L, vm.montoCentavos)
    }

    @Test
    fun `monto en el limite maximo permitido sigue siendo valido`() {
        val vm = viewModel()

        // Construye el monto maximo digito por digito: 99999999
        "99999999".forEach { vm.pulsarDigito(it.digitToInt()) }

        assertEquals(montoMaximoCentavos, vm.montoCentavos)
        assertTrue(vm.montoValido)
    }

    @Test
    fun `pulsar un digito que excederia el maximo se ignora y el monto no cambia`() {
        val vm = viewModel()
        "99999999".forEach { vm.pulsarDigito(it.digitToInt()) }

        vm.pulsarDigito(9)

        assertEquals(montoMaximoCentavos, vm.montoCentavos)
        assertTrue(vm.montoValido)
    }

    @Test
    fun `pulsar digitos construye el monto de izquierda a derecha`() {
        val vm = viewModel()

        vm.pulsarDigito(1)
        vm.pulsarDigito(2)
        vm.pulsarDigito(5)

        assertEquals(125L, vm.montoCentavos)
    }

    @Test
    fun `borrar elimina el ultimo digito capturado`() {
        val vm = viewModel()
        vm.pulsarDigito(1)
        vm.pulsarDigito(2)
        vm.pulsarDigito(5)

        vm.borrar()

        assertEquals(12L, vm.montoCentavos)
    }

    @Test
    fun `borrar en monto de un digito lo deja en cero y por lo tanto invalido`() {
        val vm = viewModel()
        vm.pulsarDigito(7)

        vm.borrar()

        assertEquals(0L, vm.montoCentavos)
        assertFalse(vm.montoValido)
    }

    @Test
    fun `el tipo de movimiento inicia en gasto y cambia al seleccionar ingreso`() {
        val vm = viewModel()

        assertEquals(TipoMovimiento.GASTO, vm.tipo)

        vm.cambiarTipo(TipoMovimiento.INGRESO)

        assertEquals(TipoMovimiento.INGRESO, vm.tipo)
    }
}
