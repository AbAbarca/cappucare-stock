package com.cappucare.app.model

/**
 * Categorías de insumos médicos. PROPUESTA: el caso no define categorías oficiales (pregunta
 * pendiente al CITT). El equipo puede ajustarlas.
 */
enum class CategoriaInsumo(val etiqueta: String) {
    CURACION("Material de curación"),
    PROTECCION("Protección personal"),
    INSTRUMENTAL("Instrumental"),
    MEDICION("Equipos de medición"),
    ORTOPEDIA("Ortopedia y movilidad"),
    HIGIENE("Higiene y desinfección"),
    OTRO("Otro")
}

enum class UnidadMedida(val etiqueta: String) {
    UNIDAD("Unidad"),
    CAJA("Caja"),
    PAQUETE("Paquete"),
    FRASCO("Frasco"),
    PAR("Par"),
    ROLLO("Rollo")
}

/**
 * Estados del producto. PROPUESTA a validar con el CITT: el caso menciona "estado del producto"
 * pero no enumera los estados posibles.
 */
enum class EstadoProducto(val etiqueta: String) {
    DISPONIBLE("Disponible"),
    AGOTADO("Agotado"),
    VENCIDO("Vencido"),
    DADO_DE_BAJA("Dado de baja")
}

/**
 * Producto o insumo médico (entidad "Producto/insumo médico" del caso).
 *
 * Regla de negocio del caso: cantidadDisponible NO se edita a mano; se deriva de los
 * movimientos de ingreso y salida (ver CappucareViewModel.registrarMovimiento()).
 *
 * TODO(equipo): cuando se implemente Room, esta clase (o una copia con @Entity) pasa a ser la
 * tabla "productos". Ver data/local/PersistenciaPendiente.kt.
 */
data class Producto(
    val id: String,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val categoria: CategoriaInsumo,
    val unidadMedida: UnidadMedida,
    val cantidadDisponible: Int = 0,
    val nivelMinimo: Int,
    val ubicacion: String,
    val estado: EstadoProducto = EstadoProducto.DISPONIBLE
)

/**
 * Errores de validación del formulario de producto (mismo patrón de la Guía 11: un campo de
 * error por cada campo del formulario; null = sin error).
 */
data class ErroresProducto(
    val codigo: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val unidadMedida: String? = null,
    val nivelMinimo: String? = null,
    val ubicacion: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(codigo, nombre, categoria, unidadMedida, nivelMinimo, ubicacion).any { it != null }
}
