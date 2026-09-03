package mx.proyecto.gastos.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import mx.proyecto.gastos.core.modelo.Categoria
import mx.proyecto.gastos.core.modelo.TipoMovimiento
import mx.proyecto.gastos.ui.theme.Background
import mx.proyecto.gastos.ui.theme.color

@Composable
fun PasoCategoria(
    tipo: TipoMovimiento,
    alElegir: (Categoria) -> Unit,
    alVolver: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header con flecha de regreso y titulo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = if (tipo == TipoMovimiento.GASTO) "¿En qué categoría?"
                       else "¿Ingreso de?",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        when (tipo) {
            TipoMovimiento.GASTO -> {
                val categoriasGasto = Categoria.entries.filter { it != Categoria.SALARIO }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                ) {
                    items(categoriasGasto) { categoria ->
                        TarjetaCategoria(
                            texto = categoria.etiqueta,
                            icono = categoria.icono,
                            colorIcono = categoria.color,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { alElegir(categoria) },
                        )
                    }
                }
            }
            TipoMovimiento.INGRESO -> {
                val todasCategorias = Categoria.entries.filter { it != Categoria.SALARIO }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    // Salario: ancho completo, fuera de la rejilla
                    TarjetaCategoria(
                        texto = Categoria.SALARIO.etiqueta,
                        icono = Categoria.SALARIO.icono,
                        colorIcono = Categoria.SALARIO.color,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { alElegir(Categoria.SALARIO) },
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    // Demas categorias en rejilla de 2 columnas
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                    ) {
                        items(todasCategorias) { categoria ->
                            TarjetaCategoria(
                                texto = categoria.etiqueta,
                                icono = categoria.icono,
                                colorIcono = categoria.color,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { alElegir(categoria) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaCategoria(
    texto: String,
    icono: ImageVector,
    colorIcono: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Background),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icono,
                contentDescription = texto,
                modifier = Modifier.size(56.dp),
                tint = colorIcono,
            )
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

private val Categoria.icono: ImageVector
    get() = when (this) {
        Categoria.COMIDA -> Icons.Filled.Restaurant
        Categoria.TRANSPORTE -> Icons.Filled.DirectionsCar
        Categoria.CASA -> Icons.Filled.Home
        Categoria.OCIO -> Icons.Filled.SportsEsports
        Categoria.SALUD -> Icons.Filled.LocalHospital
        Categoria.SALARIO -> Icons.Filled.AccountBalance
        Categoria.OTRO -> Icons.Filled.MoreHoriz
    }
