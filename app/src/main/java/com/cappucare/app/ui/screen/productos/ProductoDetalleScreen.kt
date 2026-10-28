package com.cappucare.app.ui.screen.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cappucare.app.model.EstadoProducto
import com.cappucare.app.navigation.Rutas
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.ui.components.SelectorDesplegable
import com.cappucare.app.viewmodel.CappucareViewModel

/**
 * Detalle de un producto: todos sus datos, disponibilidad actual, cambio de estado y accesos a
 * registrar un movimiento o ver su historial.
 *
 * TODO(equipo): viewModel.cambiarEstado() todavía no hace nada (RF "actualizar el estado").
 */
@Composable
fun ProductoDetalleScreen(navController: NavHostController, viewModel: CappucareViewModel) {
    val seleccionadoId by viewModel.productoSeleccionadoId.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val producto = productos.find { it.id == seleccionadoId }

    Scaffold(topBar = { BarraSuperior("Detalle del producto", navController) }) { padding ->
        if (producto == null) {
            Text("Producto no encontrado.", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        var nuevoEstado by remember(producto.id) { mutableStateOf(producto.estado) }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(producto.descripcion, style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Dato("Código", producto.codigo)
                    Dato("Categoría", producto.categoria.etiqueta)
                    Dato("Unidad de medida", producto.unidadMedida.etiqueta)
                    Dato("Cantidad disponible", producto.cantidadDisponible.toString())
                    Dato("Nivel mínimo", producto.nivelMinimo.toString())
                    Dato("Ubicación", producto.ubicacion)
                    Dato("Estado", producto.estado.etiqueta)
                }
            }

            Text("Actualizar estado", style = MaterialTheme.typography.titleSmall)
            SelectorDesplegable(
                etiqueta = "Nuevo estado",
                opciones = EstadoProducto.entries,
                seleccion = nuevoEstado,
                textoDe = { it.etiqueta },
                onSeleccion = { nuevoEstado = it }
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.cambiarEstado(producto.id, nuevoEstado) }
            ) { Text("Guardar estado") }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(Rutas.MOVIMIENTO) }) {
                Text("Registrar ingreso o salida")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(Rutas.HISTORIAL) }) {
                Text("Ver historial de este producto")
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(Rutas.PRODUCTO_FORM) }) {
                Text("Editar datos")
            }
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Text("$etiqueta: $valor", style = MaterialTheme.typography.bodyMedium)
}
