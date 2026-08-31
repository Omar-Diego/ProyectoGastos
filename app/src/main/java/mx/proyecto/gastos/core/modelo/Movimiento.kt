package mx.proyecto.gastos.core.modelo

import java.time.LocalDate

/**
 * Para referencia, el "data class" que implemento tiene la unica funcion de GUARDAR DATOS.
 *
 * Para los campos, son de lectura, para modificar se realiza una copia, como el siguiente ejemplo:
 *   - movimiento.copy(nota = "otra cosa")
 *   - "= 0L" y "= null" son valores por defecto: ese argumento es opcional al crear.
 *
 * Es la UNICA definicion de "movimiento" en el proyecto: los cuatro modulos
 * (registro, resumen, historial, datos) leen y escriben objetos de este tipo, asi
 * que cualquier cambio aqui los afecta a todos a la vez.
 *
 * Nota: "RN-x" remite a las Reglas de Negocio del documento (seccion 3.3).
 */
data class Movimiento(
    val id: Long = 0L,              // 0 = aun no guardado; la fuente de datos real asigna el id
    val montoCentavos: Long,        // RN-1: entero en centavos, nunca decimales de punto flotante
    val tipo: TipoMovimiento,       // RN-3: gasto o ingreso (ver TipoMovimiento.kt)
    val categoria: Categoria,       // RN-5: una de las seis categorias (ver Categoria.kt)
    val fecha: LocalDate,           // RN-4: fecha contable, sin hora
    val nota: String? = null        // opcional, oculta por defecto (P-2)
)
