package mx.proyecto.gastos.core.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mx.proyecto.gastos.R
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

const val CANAL_RECORDATORIOS_ID = "recordatorios_movimientos"
private const val NOTIFICACION_ID = 1001

// Crea el canal de notificación (solo necesario en Android 8+, no hace nada en versiones viejas)
fun crearCanalRecordatorios(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(
            CANAL_RECORDATORIOS_ID,
            "Recordatorios de movimientos",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Recordatorio diario para registrar tus gastos e ingresos"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(canal)
    }
}

// Construye y muestra la notificación
fun mostrarNotificacionRecordatorio(context: Context) {
    // Verificamos que el permiso ya haya sido concedido antes de intentar notificar
    val permisoConcedido = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || permisoConcedido) {
        val notificacion = NotificationCompat.Builder(context, CANAL_RECORDATORIOS_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("No olvides registrar tus movimientos")
            .setContentText("Lleva el control de tus finanzas de hoy en un par de toques.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICACION_ID, notificacion)
    }
}