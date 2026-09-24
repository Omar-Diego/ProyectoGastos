package mx.proyecto.gastos.resumen

import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CalculadoraResumenTest {

    private val hoy = LocalDate.of(2026, 9, 24)

    private fun movimiento(
        montoCentavos: Long,
        tipo: TipoMovimiento,
        fecha: LocalDate,
        categoria: Categoria = Categoria.OTRO
    ) = Movimiento(
        montoCentavos = montoCentavos,
        tipo = tipo,
        categoria = categoria,
        fecha = fecha
    )

    @Test
    fun `sin movimientos, gastado hoy, gastado del mes y disponible quedan en cero o en el presupuesto completo`() {
        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = emptyList(), hoy = hoy)

        assertEquals(0L, resumen.gastadoHoyCentavos)
        assertEquals(0L, resumen.gastadoMesCentavos)
        assertEquals(PresupuestoMensual.CENTAVOS, resumen.disponibleCentavos)
    }

    @Test
    fun `gastado hoy solo suma los gastos con fecha de hoy, ignora ingresos y gastos de otros dias`() {
        val movimientos = listOf(
            movimiento(5_000, TipoMovimiento.GASTO, hoy),
            movimiento(2_000, TipoMovimiento.GASTO, hoy),
            movimiento(50_000, TipoMovimiento.INGRESO, hoy),
            movimiento(3_000, TipoMovimiento.GASTO, hoy.minusDays(1))
        )

        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = movimientos, hoy = hoy)

        assertEquals(7_000L, resumen.gastadoHoyCentavos)
    }

    @Test
    fun `gastado del mes suma todos los gastos del mes actual sin importar el dia`() {
        val movimientos = listOf(
            movimiento(5_000, TipoMovimiento.GASTO, hoy),
            movimiento(3_000, TipoMovimiento.GASTO, hoy.minusDays(1)),
            movimiento(1_000, TipoMovimiento.GASTO, hoy.withDayOfMonth(1)),
            movimiento(50_000, TipoMovimiento.INGRESO, hoy)
        )

        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = movimientos, hoy = hoy)

        assertEquals(9_000L, resumen.gastadoMesCentavos)
    }

    @Test
    fun `gastado del mes ignora movimientos de meses distintos al actual`() {
        val movimientos = listOf(
            movimiento(5_000, TipoMovimiento.GASTO, hoy),
            movimiento(999_000, TipoMovimiento.GASTO, hoy.minusMonths(1)),
            movimiento(999_000, TipoMovimiento.GASTO, hoy.plusMonths(1))
        )

        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = movimientos, hoy = hoy)

        assertEquals(5_000L, resumen.gastadoMesCentavos)
    }

    @Test
    fun `disponible es el presupuesto del mes menos lo gastado en el mes`() {
        val movimientos = listOf(
            movimiento(150_000, TipoMovimiento.GASTO, hoy),
            movimiento(50_000, TipoMovimiento.GASTO, hoy.minusDays(2))
        )

        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = movimientos, hoy = hoy)

        assertEquals(PresupuestoMensual.CENTAVOS - 200_000L, resumen.disponibleCentavos)
    }

    @Test
    fun `disponible puede quedar en negativo cuando lo gastado supera el presupuesto`() {
        val movimientos = listOf(
            movimiento(PresupuestoMensual.CENTAVOS + 100_000L, TipoMovimiento.GASTO, hoy)
        )

        val resumen = CalculadoraResumen.calcular(todosLosMovimientos = movimientos, hoy = hoy)

        assertEquals(-100_000L, resumen.disponibleCentavos)
    }

    @Test
    fun `acepta un presupuesto personalizado distinto al valor por defecto`() {
        val movimientos = listOf(movimiento(20_000, TipoMovimiento.GASTO, hoy))
        val presupuestoPersonalizado = 100_000L

        val resumen = CalculadoraResumen.calcular(
            todosLosMovimientos = movimientos,
            hoy = hoy,
            presupuestoCentavos = presupuestoPersonalizado
        )

        assertEquals(80_000L, resumen.disponibleCentavos)
    }
}
