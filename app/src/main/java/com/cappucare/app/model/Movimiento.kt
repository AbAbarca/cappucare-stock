package com.cappucare.app.model

enum class TipoMovimiento(val etiqueta: String) {
    INGRESO("Ingreso (recepción de mercadería)"),
    SALIDA("Salida")
}

/**
 * Motivo de una salida. Regla de negocio del caso: una salida por venta debe registrarse con
 * motivo VENTA. Los demás motivos son una PROPUESTA que el equipo puede ajustar.
 */
enum class MotivoSalida(val etiqueta: String) {
    VENTA("Venta"),
    VENCIMIENTO("Vencimiento"),
    MERMA("Merma o daño"),
    AJUSTE("Ajuste de inventario"),
    OTRO("Otro")
}

/**
 * Movimiento de inventario (entidad "Movimiento de inventario" del caso).
 *
 * TODO(equipo): con Room, esta clase pasa a ser la tabla "movimientos", relacionada con
 * "productos" por productoId (clave foránea).
 */
data class Movimiento(
    val id: String,
    val productoId: String,
    val fecha: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: MotivoSalida? = null,
    val observacion: String = "",
    val responsable: String
)

/** Errores de validación del formulario de movimiento. */
data class ErroresMovimiento(
    val producto: String? = null,
    val cantidad: String? = null,
    val motivo: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(producto, cantidad, motivo).any { it != null }
}
