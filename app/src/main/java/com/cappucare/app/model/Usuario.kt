package com.cappucare.app.model

/**
 * Usuario que inicia sesión en la app. Su nombre queda registrado como "responsable" de cada
 * movimiento de inventario (RF del módulo "Movimientos de inventario").
 *
 * Solo datos ficticios (ver MockData.usuarios).
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val usuario: String,
    val clave: String,
    val perfil: Perfil
)
