package mx.proyecto.gastos.core.notificaciones

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RecordatorioWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        mostrarNotificacionRecordatorio(applicationContext)
        return Result.success()
    }
}