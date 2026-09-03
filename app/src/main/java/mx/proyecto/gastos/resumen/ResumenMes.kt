package mx.proyecto.gastos.resumen

import mx.proyecto.gastos.core.modelo.Categoria
import java.time.YearMonth

data class ResumenMes(
    val gastadoHoyCentavos: Long,
    val gastadoMesCentavos: Long,
    val ingresosMesCentavos: Long,
    val disponibleCentavos: Long,
    val variacionBalanceVsMesAnteriorPorcentaje: Double?,
    val historialSeisMeses: List<ResumenMensual>,
    val gastosPorCategoria: List<GastoPorCategoria>
) {
    companion object {
        val VACIO = ResumenMes(
            gastadoHoyCentavos = 0L, // cuanto gastaste hoy en centavos
            gastadoMesCentavos = 0L, // cuanto gastaste en el mes completo en centavos
            ingresosMesCentavos = 0L, // cuanto ingresaste en el mes
            disponibleCentavos = 0L, // presupuesto - lo que se gastó "balance total"
            variacionBalanceVsMesAnteriorPorcentaje = null, // cuanto subió o bajo el disponible vs el mes pasado, en %
            historialSeisMeses = emptyList(), // lista con los datos de los ultimos 6 meses, para la gráfica de barras
            gastosPorCategoria = emptyList() // lista con cuánto gastaste en cada categoría
        )
    }
}

data class ResumenMensual(
    val mes: YearMonth,
    val ingresosCentavos: Long,
    val gastosCentavos: Long
)

data class GastoPorCategoria(
    val categoria: Categoria,
    val montoCentavos: Long,
    val porcentaje: Double
)