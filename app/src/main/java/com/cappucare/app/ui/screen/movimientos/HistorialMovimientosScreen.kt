package com.cappucare.app.ui.screen.movimientos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cappucare.app.model.TipoMovimiento
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.viewmodel.CappucareViewModel

/**
 * Módulo "Consulta y reposición": historial de ingresos y salidas. Si se llega desde el detalle
 * de un producto muestra solo ese producto; desde el panel muestra todos.
 *
 * TODO(equipo): viewModel.historialDe() todavía no filtra por producto ni ordena por fecha.
 * Opcional: agregar filtro por tipo (ingreso/salida) o por rango de fechas.
 */
@Composable
fun HistorialMovimientosScreen(navController: NavHostController, viewModel: CappucareViewModel) {
    val seleccionadoId by viewModel.productoSeleccionadoId.collectAsState()
    val todos by viewModel.movimientos.collectAsState()
    val producto = viewModel.productoPorId(seleccionadoId)
    val historial = viewModel.historialDe(seleccionadoId)

    val titulo = if (producto != null) "Historial: ${producto.nombre}" else "Historial de movimientos"

    Scaffold(topBar = { BarraSuperior(titulo, navController) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "${historial.size} movimiento(s) · ${todos.size} en total",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(historial) { movimiento ->
                    val nombreProducto = viewModel.productoPorId(movimiento.productoId)?.nombre ?: movimiento.productoId
                    val signo = if (movimiento.tipo == TipoMovimiento.INGRESO) "+" else "−"
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("$signo${movimiento.cantidad} · $nombreProducto", style = MaterialTheme.typography.titleSmall)
                            Text(
                                movimiento.tipo.etiqueta + (movimiento.motivo?.let { " · Motivo: ${it.etiqueta}" } ?: ""),
                                style = MaterialTheme.typography.bodySmall
                            )
                            if (movimiento.observacion.isNotBlank()) {
                                Text(movimiento.observacion, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                "${movimiento.fecha} · ${movimiento.responsable}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
