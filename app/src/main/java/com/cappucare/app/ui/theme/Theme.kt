package com.cappucare.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colores PROVISIONALES (no se cuenta con un manual de marca de la organización).
// TODO(equipo): reemplazar por los colores oficiales si el CITT o la organización los entregan,
// y mantenerlos iguales a res/values/colors.xml (logo e ícono de la app).
private val VerdeCappucare = Color(0xFF00796B)
private val AzulSecundario = Color(0xFF1565C0)
private val NaranjoAlerta = Color(0xFFEF6C00)

private val LightColors = lightColorScheme(
    primary = VerdeCappucare,
    secondary = AzulSecundario,
    tertiary = NaranjoAlerta
)

private val DarkColors = darkColorScheme(
    primary = VerdeCappucare,
    secondary = AzulSecundario,
    tertiary = NaranjoAlerta
)

@Composable
fun CappucareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
