package mx.proyecto.gastos.core.modelo

/**
 * RN-5: las seis categorias fijas de la primera entrega.
 *
 * Como si fuera base de datos se guarda:
 *   - El NOMBRE de la constante (COMIDA) es lo que usa el codigo y se guarda en datos.
 *   - "etiqueta" es el texto que se muestra en pantalla a la persona.
 *
 * No se ocupa repetir la escritura, sino que siempre se muestran y obtienen los datos de la lista.
 * Se deben mandar llamar.
 */
enum class Categoria(val etiqueta: String) {
    COMIDA(etiqueta = "Comida"),
    TRANSPORTE(etiqueta = "Transporte"),
    CASA(etiqueta = "Casa"),
    OCIO(etiqueta = "Ocio"),
    SALUD(etiqueta = "Salud"),
    SALARIO(etiqueta = "Salario"),
    OTRO(etiqueta = "Otro")
}
