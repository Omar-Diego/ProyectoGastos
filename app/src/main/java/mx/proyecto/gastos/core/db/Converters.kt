package mx.proyecto.gastos.core.db

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    // Le dice a Room cómo convertir un LocalDate a algo que SQLite entienda
    @TypeConverter
    fun fromLocalDate(fecha: LocalDate?): Long? {
        return fecha?.toEpochDay()
    }

    // Le dice a Room cómo reconstruir un LocalDate a partir de ese número
    @TypeConverter
    //toEpochDay() / ofEpochDay() son funciones nativas de Kotlin/Java para LocalDate que lo convierten a un número entero de días
    fun toLocalDate(dias: Long?): LocalDate? {
        return dias?.let { LocalDate.ofEpochDay(it) }
    }
}