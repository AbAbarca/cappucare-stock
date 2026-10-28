package com.cappucare.app.ui.screen.reposicion

import androidx.compose.foundation.clickable
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
import com.cappucare.app.navigation.Rutas
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.viewmodel.CappucareViewModel

/**
 * Módulo "Consulta y reposición": productos que necesitan nueva compra (cantidad disponible
 * bajo su nivel mínimo).
 *
 * TODO(equipo):
 *  - viewModel.productosBajoMinimo() hoy devuelve una lista vacía.
 *  - (Opcional según alcance) notificación cuando un producto quede bajo su mínimo.
 */
@Composable
fun ReposicionScreen(navController: NavHostController, viewModel: CappucareViewModel) {
    // Se observa la lista para recalcular cuando cambien los productos.
    val productos by viewModel.productos.collectAsState()
    val bajoMinimo = viewModel.productosBajoMinimo()

    Scaffold(topBar = { BarraSuperior("Alertas de reposición", navController) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (bajoMinimo.isEmpty()) {
                Text(
                    "No hay productos bajo su nivel mínimo (o la regla todavía no está implementada: " +
                        "ver productosBajoMinimo() en el ViewModel). Productos revisados: ${productos.size}.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bajoMinimo) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.seleccionarProducto(producto.id)
                                navController.navigate(Rutas.PRODUCTO_DETALLE)
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Disponible: ${producto.cantidadDisponible} · Mínimo: ${producto.nivelMinimo}",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text("Ubicación: ${producto.ubicacion}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
