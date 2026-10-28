package com.cappucare.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

/**
 * Barra superior común a todas las pantallas (un solo lugar para el estilo y la navegación).
 *
 * Navegación "atrás":
 *  - El botón ATRÁS NATIVO de Android (gesto o botón del sistema) ya funciona solo: el NavHost
 *    de Navigation Compose se conecta automáticamente al back del sistema y saca la pantalla
 *    actual de la pila (back stack). No hay que programar nada para eso.
 *  - Esta barra agrega además la flecha "←" visible arriba a la izquierda, que hace lo mismo
 *    (navController.popBackStack()). Solo aparece si hay una pantalla anterior a la cual volver
 *    — por eso no aparece en el panel principal después del login (el login
 *    se saca de la pila al ingresar, para que "atrás" no devuelva al login).
 *
 * @param onCerrarSesion si se entrega, muestra el botón "Cerrar sesión" a la derecha.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    navController: NavHostController,
    onCerrarSesion: (() -> Unit)? = null
) {
    val hayPantallaAnterior = navController.previousBackStackEntry != null

    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            if (hayPantallaAnterior) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        actions = {
            if (onCerrarSesion != null) {
                TextButton(
                    onClick = onCerrarSesion,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text("Cerrar sesión") }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
