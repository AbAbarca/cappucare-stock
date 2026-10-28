package com.cappucare.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cappucare.app.navigation.Rutas
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.viewmodel.CappucareViewModel
import androidx.navigation.NavHostController

/**
 * Panel principal después del login: resumen del inventario y acceso a cada módulo.
 *
 * TODO(equipo): si el CITT confirma que "Usuario autorizado" tiene menos permisos que
 * "Personal encargado", ocultar aquí (y en Productos) las acciones que no le correspondan.
 */
@Composable
fun PanelScreen(
    navController: NavHostController,
    viewModel: CappucareViewModel,
    onCerrarSesion: () -> Unit
) {
    val usuario by viewModel.usuarioActivo.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val movimientos by viewModel.movimientos.collectAsState()
    val bajoMinimo = viewModel.productosBajoMinimo()

    Scaffold(topBar = { BarraSuperior("Cappucare Stock", navController, onCerrarSesion) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Hola, ${usuario?.nombre.orEmpty()}", style = MaterialTheme.typography.titleLarge)
            Text("Perfil: ${usuario?.perfil?.etiqueta.orEmpty()}", style = MaterialTheme.typography.bodyMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                TarjetaResumen("Productos", productos.size.toString(), Modifier.weight(1f))
                TarjetaResumen("Bajo mínimo", bajoMinimo.size.toString(), Modifier.weight(1f))
                TarjetaResumen("Movimientos", movimientos.size.toString(), Modifier.weight(1f))
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(Rutas.PRODUCTOS) }) {
                Text("Productos e insumos")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                viewModel.seleccionarProducto(null)
                navController.navigate(Rutas.MOVIMIENTO)
            }) { Text("Registrar ingreso o salida") }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(Rutas.REPOSICION) }) {
                Text("Alertas de reposición")
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                viewModel.seleccionarProducto(null)
                navController.navigate(Rutas.HISTORIAL)
            }) { Text("Historial de movimientos") }
        }
    }
}

@Composable
private fun TarjetaResumen(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(valor, style = MaterialTheme.typography.headlineSmall)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
        }
    }
}
