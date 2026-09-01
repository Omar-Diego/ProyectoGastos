package mx.proyecto.gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import mx.proyecto.gastos.core.repo.MovimientoRepository
import mx.proyecto.gastos.core.repo.RepositorioDePrueba
import mx.proyecto.gastos.nav.App
import mx.proyecto.gastos.ui.theme.ProyectoGastos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositorio: MovimientoRepository = RepositorioDePrueba()
        setContent {
            ProyectoGastos {
                App(repositorio)
            }
        }
    }
}
