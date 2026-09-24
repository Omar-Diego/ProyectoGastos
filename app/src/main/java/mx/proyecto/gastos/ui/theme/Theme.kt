package mx.proyecto.gastos.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AzulOscuroPrincipal,
    onPrimary = Color.White,
    primaryContainer = AzulOscuroSecundario,
    onPrimaryContainer = Color.White,

    secondary = AzulOscuroSecundario,
    onSecondary = Subtitulos,

    background = BlackBackground,
    onBackground = Color.White,

    surface = BlackBackground,
    onSurface = Color.White,

    //Color de las tarjetas en modo oscuro
    surfaceVariant = Color(0xFF504F4F),
    onSurfaceVariant = Color.White,

    error = Rojo,

    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = Color.White,

    secondary = AzulClaro,
    onSecondary = Color.White,

    background = Background,
    onBackground = TextColor,

    surface = Background,
    onSurface = TextColor,

    //Color de las tarjetas en modo claro
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = TextColor,

    error = Rojo,

    onError = Color.White
)


@Composable
fun ProyectoGastos(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}