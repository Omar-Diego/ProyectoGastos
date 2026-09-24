package mx.proyecto.gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import mx.proyecto.gastos.core.notificaciones.RecordatorioWorker
import mx.proyecto.gastos.core.notificaciones.crearCanalRecordatorios
import mx.proyecto.gastos.core.repo.MovimientoRepository
import mx.proyecto.gastos.core.repo.MovimientoRepositoryRoom
import mx.proyecto.gastos.core.db.AppDatabase
import mx.proyecto.gastos.nav.App
import mx.proyecto.gastos.ui.theme.ProyectoGastos
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val pedirPermisoNotificaciones = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        // No importa si dice sí o no, en ambos casos ya podemos programar la tarea:
        // WorkManager internamente respeta si el permiso fue negado.
        programarRecordatorioDiario()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(this)
        val repositorio: MovimientoRepository = MovimientoRepositoryRoom(dao = db.movimientoDao())

        crearCanalRecordatorios(this)
        pedirPermisoNotificaciones.launch(android.Manifest.permission.POST_NOTIFICATIONS)

        setContent {
            ProyectoGastos {
                App(repositorio)
            }
        }
    }

    private fun programarRecordatorioDiario() {
        val solicitud = PeriodicWorkRequestBuilder<RecordatorioWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "recordatorio_diario_movimientos",
            ExistingPeriodicWorkPolicy.KEEP,
            solicitud
        )
    }
}