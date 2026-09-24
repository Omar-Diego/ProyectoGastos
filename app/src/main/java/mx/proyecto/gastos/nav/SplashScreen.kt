package mx.proyecto.gastos.nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import mx.proyecto.gastos.R

private const val DURACION_SPLASH_MS = 3000L

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(DURACION_SPLASH_MS)
        navController.navigate(Destino.RESUMEN.ruta) {
            popUpTo(RUTA_SPLASH) { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.ic_launcher_background)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.logo_finanzas),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .padding(24.dp)
            )
        }
    }
}
