package mx.proyecto.gastos.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
            composable(route = Destino.REGISTRO.ruta) {RegistroScreen()}
            composable(route = Destino.HISTORIAL.ruta) {HistorialScreen()}
        }
    }
}

@Composable
private fun BarraInferior(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    NavigationBar {
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
                label = { Text(destino.etiqueta) }
            )
        }
    }
}