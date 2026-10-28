package com.cappucare.app.model

/**
 * Perfiles definidos en el caso (ver Requerimientos_Seccion001, "Actores").
 *
 * Pendiente de confirmar con el CITT (ver Consultas_Pendientes_Seccion001): si "Personal
 * encargado" y "Otros usuarios autorizados" tienen permisos distintos, o si son la misma
 * función. Mientras tanto se modelan como dos perfiles separados.
 */
enum class Perfil(val etiqueta: String) {
    ENCARGADO("Personal encargado"),
    USUARIO_AUTORIZADO("Usuario autorizado")
}
