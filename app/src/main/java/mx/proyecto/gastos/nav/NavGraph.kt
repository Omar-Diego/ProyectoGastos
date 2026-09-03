package mx.proyecto.gastos.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.proyecto.gastos.core.repo.MovimientoRepository
import mx.proyecto.gastos.historial.HistorialScreen
import mx.proyecto.gastos.registro.RegistroScreen
import mx.proyecto.gastos.resumen.ResumenScreen
import androidx.compose.ui.geometry.Offset
import mx.proyecto.gastos.ui.theme.AzulClaro
import mx.proyecto.gastos.ui.theme.AzulPrincipal
import mx.proyecto.gastos.ui.theme.TextColor

enum class Destino(val ruta: String, val etiqueta: String, val icono: ImageVector){
    RESUMEN(ruta = "resumen", etiqueta = "Resumen",  icono = Icons.Filled.Home),
    REGISTRO(ruta = "registro", etiqueta = "Registro", icono = Icons.Filled.Add),
    HISTORIAL(ruta = "historial", etiqueta = "Historial", icono = Icons.AutoMirrored.Filled.List)
}

@Composable
fun App(repositorio: MovimientoRepository){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {BarraInferior(navController)}
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destino.RESUMEN.ruta,
            modifier = Modifier.padding(padding)
        ) {
            composable(route = Destino.RESUMEN.ruta) {ResumenScreen()}
            composable(route = Destino.REGISTRO.ruta) {
                RegistroScreen(
                    repositorio = repositorio,
                    alGuardar = {
                        navController.navigate(Destino.RESUMEN.ruta) {
                            popUpTo(Destino.REGISTRO.ruta) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(route = Destino.HISTORIAL.ruta) {HistorialScreen()}
        }
    }
}

@Composable
private fun BarraInferior(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val borderColor = Color.LightGray

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .drawBehind{
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = borderColor,
                    start = Offset(0f,0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }

    ) {
        Destino.entries.forEach { destino ->
            NavigationBarItem(
                selected = rutaActual == destino.ruta,
                onClick = {
                    navController.navigate(destino.ruta) {
                        val rutaInicio = navController.graph.findStartDestination().route
                        if (rutaInicio != null) {
                            popUpTo(rutaInicio) { saveState = true }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = destino.icono, contentDescription = destino.etiqueta) },
                label = {
                    Text(
                        destino.etiqueta,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrincipal,
                    selectedTextColor = AzulPrincipal,
                    indicatorColor = AzulClaro.copy(alpha = 0.2f),

                    unselectedIconColor = TextColor.copy(alpha = 0.5f),
                    unselectedTextColor = TextColor.copy(alpha = 0.5f)
                )
            )
        }
    }
}