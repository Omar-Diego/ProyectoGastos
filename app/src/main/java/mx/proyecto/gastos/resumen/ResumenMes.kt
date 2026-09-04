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
    val semanasMes: List<ResumenSemanal>,
    val gastosPorCategoria: List<GastoPorCategoria>,
    val gastosDiarios: List<GastoDiario>,
    val gastoPromedioDiarioCentavos: Long
) {
    companion object {
        val VACIO = ResumenMes(
            gastadoHoyCentavos = 0L,
            gastadoMesCentavos = 0L,
            ingresosMesCentavos = 0L,
            disponibleCentavos = 0L,
            variacionBalanceVsMesAnteriorPorcentaje = null,
            historialSeisMeses = emptyList(),
            semanasMes = emptyList(),
            gastosPorCategoria = emptyList(),
            gastosDiarios = emptyList(),
            gastoPromedioDiarioCentavos = 0L
        )
    }
}

data class GastoDiario(
    val dia: Int,
    val montoCentavos: Long
)

data class ResumenMensual(
    val mes: YearMonth,
    val ingresosCentavos: Long,
    val gastosCentavos: Long
)

data class ResumenSemanal(
    val semana: Int,
    val etiqueta: String,
    val ingresosCentavos: Long,
    val gastosCentavos: Long
)

data class GastoPorCategoria(
    val categoria: Categoria,
    val montoCentavos: Long,
    val porcentaje: Double
)