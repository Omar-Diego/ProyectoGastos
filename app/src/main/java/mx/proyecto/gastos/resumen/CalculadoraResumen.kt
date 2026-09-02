package mx.proyecto.gastos.resumen

import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import java.time.LocalDate
import java.time.YearMonth

object CalculadoraResumen {

    /**
     *
     */

    fun calcular(
        movimientosDelMes: List<Movimiento>,
        hoy: LocalDate,
        presupuestoCentavos: Long = PresupuestoMensual.CENTAVOS
    ): ResumenMes {

        val mesEnCurso = YearMonth.from(hoy)

        val gastos = movimientosDelMes.filter { it.tipo == TipoMovimiento.GASTO }
        val ingresos = movimientosDelMes.filter { it.tipo == TipoMovimiento.INGRESO }

        // Todo el gasto del mes
        val gastadoMes = gastos.sumOf { it.montoCentavos }


        // Solo el gasto cuya fecha sea hoy
        // Si se está viendo un mes que no es el actual, "hoy" no cae en él y da 0
        val gastadoHoy = gastos
            .filter { it.fecha == hoy && YearMonth.from(it.fecha) == mesEnCurso }
            .sumOf { it.montoCentavos }

        // los ingresos del van por separado
        val ingresosMes = ingresos.sumOf { it.montoCentavos }


        // presupuesto - gasto. NO entra el ingreso
        val disponible = presupuestoCentavos - gastadoMes

        return ResumenMes(
            gastadoHoyCentavos = gastadoHoy,
            gastadoMesCentavos = gastadoMes,
            disponibleCentavos = disponible,
            ingresosMesCentavos = ingresosMes
        )
    }
}