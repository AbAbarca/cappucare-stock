package com.cappucare.app.ui.screen.productos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.cappucare.app.model.CategoriaInsumo
import com.cappucare.app.navigation.Rutas
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.ui.components.SelectorDesplegable
import com.cappucare.app.viewmodel.CappucareViewModel

/**
 * Módulos "Gestión de productos" y "Consulta y reposición": catálogo con búsqueda por
 * nombre/código y filtro por categoría. Tocar un producto abre su detalle; el botón "+" abre
 * el formulario de producto nuevo.
 *
 * TODO(equipo):
 *  - viewModel.buscarProductos() todavía no filtra (ver ViewModel).
 *  - Marcar visualmente los productos bajo su nivel mínimo (por ejemplo, texto en color de
 *    alerta), una vez implementada la regla en el ViewModel.
 *  - (Opcional según alcance) buscar escaneando un código QR/de barras con la cámara.
 */
@Composable
fun ProductosScreen(navController: NavHostController, viewModel: CappucareViewModel) {
    // Se observa la lista para que la pantalla se redibuje cuando cambie.
    val todos by viewModel.productos.collectAsState()
    var texto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf<CategoriaInsumo?>(null) }
    val resultado = remember(todos, texto, categoria) { viewModel.buscarProductos(texto, categoria) }

    Scaffold(
        topBar = { BarraSuperior("Productos e insumos", navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.seleccionarProducto(null)
                navController.navigate(Rutas.PRODUCTO_FORM)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Buscar por nombre o código") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            SelectorDesplegable<CategoriaInsumo?>(
                etiqueta = "Categoría",
                opciones = listOf<CategoriaInsumo?>(null) + CategoriaInsumo.entries,
                seleccion = categoria,
                textoDe = { it?.etiqueta ?: "Todas las categorías" },
                onSeleccion = { categoria = it },
                textoVacio = "Todas las categorías",
                modifier = Modifier.padding(top = 8.dp)
            )

            if (resultado.isEmpty()) {
                Text(
                    "No hay productos para mostrar.",
                    modifier = Modifier.padding(top = 24.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(resultado) { producto ->
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
                            Text("${producto.codigo} · ${producto.categoria.etiqueta}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                "Disponible: ${producto.cantidadDisponible} ${producto.unidadMedida.etiqueta.lowercase()} · Mínimo: ${producto.nivelMinimo}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text("Estado: ${producto.estado.etiqueta}", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
