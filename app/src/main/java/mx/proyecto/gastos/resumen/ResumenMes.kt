package mx.proyecto.gastos.resumen

/**
 * Las cuatro cifras del resumen del mes en curso, en centavos
 * "disponible" puede ser negativo si se gastó de más
 * "ingresos" va apart, no se mezcla con el gasto
 */
data class ResumenMes(
    val gastadoHoyCentavos: Long,
    val gastadoMesCentavos: Long,
    val disponibleCentavos: Long,
    val ingresosMesCentavos: Long,
){
    companion object{
        val VACIO = ResumenMes(0L, 0L, 0L, 0L)
    }
}