package mx.proyecto.gastos.ui.theme

import androidx.compose.ui.graphics.Color
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.TipoMovimiento

val AzulPrincipal = Color(0xFF053394)
val AzulClaro = Color(0xFF2592E8)
val Background = Color(0xFFFFFFFF)

val Verde = Color(0xFF0EAF70)
val Rojo = Color(0xFFC42830)

val Text = Color(0xFF1E1E1E)

val CategoriaTransporte = Color(0xFF76B5EB)
val CategoriaSalud = Color(0xFFEB5E5A)
val CategoriaOcio = Color(0xFFF897D5)
val CategoriaComida = Color(0xFFEEEC7D)
val CategoriaSalario = Color(0xFF66B47B)
val CategoriaCasa = Color(0xFF78EAE3)
val CategoriaOtro = Color(0xFFB5EA78)

val Categoria.color: Color
    get() = when (this) {
        Categoria.COMIDA -> CategoriaComida
        Categoria.TRANSPORTE -> CategoriaTransporte
        Categoria.CASA -> CategoriaCasa
        Categoria.OCIO -> CategoriaOcio
        Categoria.SALUD -> CategoriaSalud
        Categoria.SALARIO -> CategoriaSalario
        Categoria.OTRO -> CategoriaOtro
    }

val TipoMovimiento.color: Color
    get() = when (this) {
        TipoMovimiento.INGRESO -> Verde
        TipoMovimiento.GASTO -> Rojo
    }