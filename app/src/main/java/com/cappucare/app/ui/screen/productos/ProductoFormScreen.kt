package com.cappucare.app.ui.screen.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cappucare.app.model.CategoriaInsumo
import com.cappucare.app.model.ErroresProducto
import com.cappucare.app.model.EstadoProducto
import com.cappucare.app.model.Producto
import com.cappucare.app.model.UnidadMedida
import com.cappucare.app.ui.components.BarraSuperior
import com.cappucare.app.ui.components.SelectorDesplegable
import com.cappucare.app.viewmodel.CappucareViewModel

/**
 * Formulario de producto (nuevo o editar). RF: "ingresar un nuevo producto/insumo con código,
 * nombre, descripción, categoría, unidad de medida, nivel mínimo de existencias y ubicación".
 *
 * La pantalla ya muestra los errores por campo (isError + supportingText, patrón Guía 11),
 * pero viewModel.validarProducto() y viewModel.guardarProducto() son TODO(equipo).
 *
 * La cantidad disponible NO está en el formulario a propósito: según la regla de negocio, se
 * calcula con los movimientos de ingreso y salida.
 */
@Composable
fun ProductoFormScreen(navController: NavHostController, viewModel: CappucareViewModel) {
    val seleccionadoId by viewModel.productoSeleccionadoId.collectAsState()
    val existente = remember(seleccionadoId) { viewModel.productoPorId(seleccionadoId) }

    var codigo by remember { mutableStateOf(existente?.codigo ?: "") }
    var nombre by remember { mutableStateOf(existente?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(existente?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(existente?.categoria) }
    var unidad by remember { mutableStateOf(existente?.unidadMedida) }
    var nivelMinimo by remember { mutableStateOf(existente?.nivelMinimo?.toString() ?: "") }
    var ubicacion by remember { mutableStateOf(existente?.ubicacion ?: "") }
    var errores by remember { mutableStateOf(ErroresProducto()) }

    val titulo = if (existente == null) "Nuevo producto" else "Editar producto"

    Scaffold(topBar = { BarraSuperior(titulo, navController) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoTexto("Código", codigo, { codigo = it }, errores.codigo)
            CampoTexto("Nombre", nombre, { nombre = it }, errores.nombre)
            CampoTexto("Descripción", descripcion, { descripcion = it }, null, unaLinea = false)

            SelectorDesplegable(
                etiqueta = "Categoría",
                opciones = CategoriaInsumo.entries,
                seleccion = categoria,
                textoDe = { it.etiqueta },
                onSeleccion = { categoria = it },
                error = errores.categoria
            )
            SelectorDesplegable(
                etiqueta = "Unidad de medida",
                opciones = UnidadMedida.entries,
                seleccion = unidad,
                textoDe = { it.etiqueta },
                onSeleccion = { unidad = it },
                error = errores.unidadMedida
            )

            CampoTexto("Nivel mínimo de existencias", nivelMinimo, { nivelMinimo = it }, errores.nivelMinimo, numerico = true)
            CampoTexto("Ubicación de almacenamiento", ubicacion, { ubicacion = it }, errores.ubicacion)

            if (existente != null) {
                Text(
                    "Cantidad disponible: ${existente.cantidadDisponible} (se actualiza solo con movimientos)",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = {
                    errores = viewModel.validarProducto(codigo, nombre, categoria, unidad, nivelMinimo, ubicacion)
                    val categoriaElegida = categoria
                    val unidadElegida = unidad
                    if (!errores.hayErrores && categoriaElegida != null && unidadElegida != null) {
                        viewModel.guardarProducto(
                            Producto(
                                id = existente?.id ?: "",
                                codigo = codigo.trim(),
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoriaElegida,
                                unidadMedida = unidadElegida,
                                cantidadDisponible = existente?.cantidadDisponible ?: 0,
                                nivelMinimo = nivelMinimo.toIntOrNull() ?: 0,
                                ubicacion = ubicacion.trim(),
                                estado = existente?.estado ?: EstadoProducto.DISPONIBLE
                            )
                        )
                        navController.popBackStack()
                    }
                }
            ) { Text("Guardar") }
        }
    }
}

/** Campo de texto con mensaje de error debajo (isError + supportingText). */
@Composable
private fun CampoTexto(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    error: String?,
    unaLinea: Boolean = true,
    numerico: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        singleLine = unaLinea,
        minLines = if (unaLinea) 1 else 2,
        isError = error != null,
        supportingText = if (error != null) {
            { Text(error) }
        } else {
            null
        },
        keyboardOptions = if (numerico) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        modifier = Modifier.fillMaxWidth()
    )
}
