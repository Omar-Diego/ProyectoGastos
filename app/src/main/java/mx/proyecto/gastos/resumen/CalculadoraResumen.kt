package mx.proyecto.gastos.resumen

import mx.proyecto.gastos.core.modelo.Movimiento
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

object CalculadoraResumen {

    fun calcular(
        todosLosMovimientos: List<Movimiento>,
        hoy: LocalDate,
        presupuestoCentavos: Long = PresupuestoMensual.CENTAVOS
    ): ResumenMes {

        val mesActual = YearMonth.from(hoy)
        val mesAnterior = mesActual.minusMonths(1)

        val movimientosMesActual = todosLosMovimientos.filter { YearMonth.from(it.fecha) == mesActual }
        val movimientosMesAnterior = todosLosMovimientos.filter { YearMonth.from(it.fecha) == mesAnterior }

        val gastosMesActual = movimientosMesActual.filter { it.tipo == TipoMovimiento.GASTO }
        val ingresosMesActual = movimientosMesActual.filter { it.tipo == TipoMovimiento.INGRESO }
        val gastosMesAnterior = movimientosMesAnterior.filter { it.tipo == TipoMovimiento.GASTO }
        val ingresosMesAnterior = movimientosMesAnterior.filter { it.tipo == TipoMovimiento.INGRESO }

        val gastadoMes = gastosMesActual.sumOf { it.montoCentavos }
        val ingresosMes = ingresosMesActual.sumOf { it.montoCentavos }
        val gastadoMesAnterior = gastosMesAnterior.sumOf { it.montoCentavos }
        val ingresosMesAnteriorTotal = ingresosMesAnterior.sumOf { it.montoCentavos }

        val gastadoHoy = gastosMesActual
            .filter { it.fecha == hoy }
            .sumOf { it.montoCentavos }

        val disponible = presupuestoCentavos - gastadoMes

        val balanceActual = ingresosMes - gastadoMes
        val balanceAnterior = ingresosMesAnteriorTotal - gastadoMesAnterior

        val variacion = if (balanceAnterior != 0L) {
            ((balanceActual - balanceAnterior).toDouble() / kotlin.math.abs(balanceAnterior).toDouble()) * 100.0
        } else {
            null
        }

        val historial = (5 downTo 0).map { mesesAtras ->
            val mes = mesActual.minusMonths(mesesAtras.toLong())
            val movimientosDelMes = todosLosMovimientos.filter { YearMonth.from(it.fecha) == mes }
            ResumenMensual(
                mes = mes,
                ingresosCentavos = movimientosDelMes.filter { it.tipo == TipoMovimiento.INGRESO }.sumOf { it.montoCentavos },
                gastosCentavos = movimientosDelMes.filter { it.tipo == TipoMovimiento.GASTO }.sumOf { it.montoCentavos }
            )
        }

        val gastosPorCategoria = gastosMesActual
            .groupBy { it.categoria }
            .map { (categoria, movimientos) ->
                val monto = movimientos.sumOf { it.montoCentavos }
                val porcentaje = if (gastadoMes != 0L) (monto.toDouble() / gastadoMes.toDouble()) * 100.0 else 0.0
                GastoPorCategoria(categoria, monto, porcentaje)
            }
            .sortedByDescending { it.montoCentavos }

        // Gastos diarios del mes actual
        val gastosDiarios = gastosMesActual
            .groupBy { it.fecha.dayOfMonth }
            .map { (dia, movimientos) ->
                GastoDiario(dia, movimientos.sumOf { it.montoCentavos })
            }
            .sortedBy { it.dia }

        // Semanas del mes actual
        val weekFields = WeekFields.of(Locale("es", "MX"))
        val primeraSemana = movimientosMesActual.minOfOrNull { it.fecha.get(weekFields.weekOfWeekBasedYear()) } ?: 0
        val semanasMes = movimientosMesActual
            .groupBy { it.fecha.get(weekFields.weekOfWeekBasedYear()) - primeraSemana }
            .map { (semanaIdx, movimientos) ->
                val semanaNum = semanaIdx + 1
                val etiqueta = "Sem $semanaNum"
                ResumenSemanal(
                    semana = semanaNum,
                    etiqueta = etiqueta,
                    ingresosCentavos = movimientos.filter { it.tipo == TipoMovimiento.INGRESO }.sumOf { it.montoCentavos },
                    gastosCentavos = movimientos.filter { it.tipo == TipoMovimiento.GASTO }.sumOf { it.montoCentavos }
                )
            }
            .sortedBy { it.semana }

        // Gasto promedio diario
        val diasTranscurridos = hoy.dayOfMonth
        val gastoPromedioDiario = if (diasTranscurridos > 0) gastadoMes / diasTranscurridos else 0L

        return ResumenMes(
            gastadoHoyCentavos = gastadoHoy,
            gastadoMesCentavos = gastadoMes,
            ingresosMesCentavos = ingresosMes,
            disponibleCentavos = disponible,
            variacionBalanceVsMesAnteriorPorcentaje = variacion,
            historialSeisMeses = historial,
            semanasMes = semanasMes,
            gastosPorCategoria = gastosPorCategoria,
            gastosDiarios = gastosDiarios,
            gastoPromedioDiarioCentavos = gastoPromedioDiario
        )
    }
}