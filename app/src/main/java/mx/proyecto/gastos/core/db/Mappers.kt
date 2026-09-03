package mx.proyecto.gastos.core.db

import mx.proyecto.gastos.core.modelo.Movimiento

fun Movimiento.toEntity(): MovimientoEntity {
    return MovimientoEntity(
        id = id,
        montoCentavos = montoCentavos,
        tipo = tipo,
        categoria = categoria,
        fecha = fecha,
        nota = nota
    )
}

fun MovimientoEntity.toDominio(): Movimiento {
    return Movimiento(
        id = id,
        montoCentavos = montoCentavos,
        tipo = tipo,
        categoria = categoria,
        fecha = fecha,
        nota = nota
    )
}