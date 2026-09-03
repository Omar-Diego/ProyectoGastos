package mx.proyecto.gastos.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.proyecto.gastos.ui.theme.AzulClaro

/**
 * Color azul estilo crayola para la flecha y texto de acción.
 * Más vibrante y juguetón que AzulPrincipal, ideal para guiar al usuario.
 */
val AzulCrayola = AzulClaro

/**
 * Componente reutilizable para mostrar un estado vacío cuando no hay datos.
 * Incluye un icono, título, descripción y una flecha animada que apunta
 * al botón de registro para guiar al usuario.
 */
@Composable
fun EmptyState(
    icon: ImageVector = Icons.Outlined.AccountBalanceWallet,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    // Animación de rebote para la flecha
    val infiniteTransition = rememberInfiniteTransition(label = "flecha_animada")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset_flecha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = AzulCrayola.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Flecha animada apuntando hacia abajo (al botón de Registro)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset { IntOffset(0, animatedOffset.dp.roundToPx()) }
        ) {
            Text(
                text = "↓",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = AzulCrayola
            )
            Text(
                text = "Registra",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = AzulCrayola
            )
        }
    }
}
