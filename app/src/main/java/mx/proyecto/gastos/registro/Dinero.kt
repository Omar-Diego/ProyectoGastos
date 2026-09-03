package mx.proyecto.gastos.registro

// El monto SIEMPRE se guarda como entero de centavos.
// Funcion que convierte los centavos en el texto mostrable

fun Long.pesos(): String{
    val entero = this / 100
    val centavos = this % 100
    return "\$%,d.%02d".format(entero, centavos)
}