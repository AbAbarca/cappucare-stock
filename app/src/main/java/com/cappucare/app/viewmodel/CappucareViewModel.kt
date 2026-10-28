package com.cappucare.app.viewmodel

import androidx.lifecycle.ViewModel
import com.cappucare.app.data.MockData
import com.cappucare.app.model.CategoriaInsumo
import com.cappucare.app.model.ErroresMovimiento
import com.cappucare.app.model.ErroresProducto
import com.cappucare.app.model.EstadoProducto
import com.cappucare.app.model.MotivoSalida
import com.cappucare.app.model.Movimiento
import com.cappucare.app.model.Perfil
import com.cappucare.app.model.Producto
import com.cappucare.app.model.TipoMovimiento
import com.cappucare.app.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel único, compartido por todas las pantallas (se crea una sola vez en AppNavigation).
 *
 * ESTO ES UN ESQUELETO: las pantallas ya llaman a todas estas funciones, pero la lógica de
 * negocio del caso está vacía a propósito y marcada con TODO(equipo), citando el módulo de
 * Requerimientos_Seccion001 al que corresponde. Mientras no se implemente:
 *  - el login siempre rechaza (usar "Modo desarrollo" en la pantalla de login),
 *  - guardar productos, cambiar estados y registrar movimientos no hace nada,
 *  - búsquedas, filtros, historial por producto y alertas de reposición no filtran.
 */
class CappucareViewModel : ViewModel() {

    // =====================================================================
    // Autenticación
    // =====================================================================

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    /**
     * TODO(equipo): validar usuario y clave contra MockData.usuarios (o el backend que confirme
     * el CITT). Si son correctos: dejar _usuarioActivo con ese usuario y devolver true. Si no:
     * dejar _errorLogin con un mensaje claro y devolver false.
     * Antes de comparar credenciales, validar campos vacíos (patrón de la Guía 11).
     */
    fun login(usuario: String, clave: String): Boolean {
        _errorLogin.value = "Login sin implementar — ver TODO(equipo) en CappucareViewModel.login()."
        return false
    }

    /**
     * SOLO PARA DESARROLLO: entra con el usuario de prueba del perfil indicado, sin validar
     * credenciales, para poder recorrer las pantallas.
     * TODO(equipo): eliminar esta función y los botones "Modo desarrollo" antes de entregar.
     */
    fun entrarModoDesarrollo(perfil: Perfil) {
        _usuarioActivo.value = MockData.usuarios.first { it.perfil == perfil }
        _errorLogin.value = null
    }

    fun logout() {
        _usuarioActivo.value = null
        _errorLogin.value = null
        _productoSeleccionadoId.value = null
    }

    // =====================================================================
    // Módulo "Gestión de productos"
    // =====================================================================

    private val _productos = MutableStateFlow(MockData.productos)
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    /** Producto elegido en una lista (para abrir su detalle, editarlo o ver su historial). */
    private val _productoSeleccionadoId = MutableStateFlow<String?>(null)
    val productoSeleccionadoId: StateFlow<String?> = _productoSeleccionadoId.asStateFlow()

    fun seleccionarProducto(id: String?) {
        _productoSeleccionadoId.value = id
    }

    fun productoPorId(id: String?): Producto? = _productos.value.find { it.id == id }

    /**
     * TODO(equipo): filtrar por texto (nombre o código, sin distinguir mayúsculas) y por
     * categoría (null = todas). Hoy devuelve la lista completa sin filtrar.
     * RF: "consultar la disponibilidad actual de cualquier producto" / "clasificar por categoría".
     */
    fun buscarProductos(texto: String, categoria: CategoriaInsumo?): List<Producto> = _productos.value

    /**
     * TODO(equipo): validar el formulario de producto y devolver los errores por campo
     * (campos obligatorios, nivel mínimo numérico y mayor o igual a 0, código no repetido, etc.).
     * Hoy devuelve "sin errores" siempre.
     */
    fun validarProducto(
        codigo: String,
        nombre: String,
        categoria: CategoriaInsumo?,
        unidadMedida: com.cappucare.app.model.UnidadMedida?,
        nivelMinimo: String,
        ubicacion: String
    ): ErroresProducto = ErroresProducto()

    /**
     * TODO(equipo): crear el producto si es nuevo (id == "") o actualizarlo si ya existe.
     * RF: "ingresar un nuevo producto/insumo con código, nombre, descripción, categoría, unidad
     * de medida, nivel mínimo de existencias y ubicación de almacenamiento".
     * Recordar la regla de negocio: la cantidad disponible NO se edita desde este formulario.
     * Hoy no hace nada: el producto no aparece en la lista.
     */
    fun guardarProducto(producto: Producto) {
        // Sin implementar todavía.
    }

    /**
     * TODO(equipo): cambiar el estado del producto. RF: "actualizar el estado de un producto"
     * (los estados exactos están pendientes de confirmar con el CITT). Hoy no hace nada.
     */
    fun cambiarEstado(productoId: String, nuevoEstado: EstadoProducto) {
        // Sin implementar todavía.
    }

    // =====================================================================
    // Módulo "Movimientos de inventario"
    // =====================================================================

    private val _movimientos = MutableStateFlow(MockData.movimientos)
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    /**
     * RF-04 / RF-05: valida el formulario de movimiento y devuelve un error por campo.
     *  - Producto obligatorio.
     *  - Cantidad: número entero mayor que 0.
     *  - SALIDA: motivo obligatorio, la cantidad no puede superar lo disponible (RN-03)
     *    y no se vende un producto VENCIDO o DADO_DE_BAJA.
     */
    fun validarMovimiento(
        productoId: String?,
        tipo: TipoMovimiento,
        cantidad: String,
        motivo: MotivoSalida?
    ): ErroresMovimiento {
        val producto = productoPorId(productoId)
        val cantidadNumero = cantidad.trim().toIntOrNull()

        val errorProducto = if (producto == null) "Elige un producto" else null

        var errorCantidad: String? = null
        if (cantidadNumero == null || cantidadNumero <= 0) {
            errorCantidad = "Ingresa una cantidad mayor que 0"
        } else if (tipo == TipoMovimiento.SALIDA && producto != null &&
            cantidadNumero > producto.cantidadDisponible
        ) {
            errorCantidad = "Solo hay ${producto.cantidadDisponible} disponibles"
        }

        var errorMotivo: String? = null
        if (tipo == TipoMovimiento.SALIDA) {
            if (motivo == null) {
                errorMotivo = "Elige el motivo de la salida"
            } else if (motivo == MotivoSalida.VENTA && producto != null &&
                (producto.estado == EstadoProducto.VENCIDO || producto.estado == EstadoProducto.DADO_DE_BAJA)
            ) {
                errorMotivo = "No se puede vender un producto ${producto.estado.etiqueta.lowercase()}"
            }
        }

        return ErroresMovimiento(
            producto = errorProducto,
            cantidad = errorCantidad,
            motivo = errorMotivo
        )
    }

    /**
     * RF-04 / RF-05 / RF-06 / RF-07: registra el movimiento y actualiza el stock del producto.
     *  - El responsable sale del usuario activo (no se escribe a mano) -> RF-06 / RN-05.
     *  - La fecha y hora se generan aquí, en formato ordenable.
     *  - INGRESO suma, SALIDA resta -> RN-01. El motivo solo se guarda en salidas -> RN-02.
     *  - Se vuelve a comprobar que el stock no quede negativo (RN-03), porque la pantalla
     *    no es la única forma de llamar a esta función.
     */
    fun registrarMovimiento(
        productoId: String,
        tipo: TipoMovimiento,
        cantidad: Int,
        motivo: MotivoSalida?,
        observacion: String
    ) {
        val usuario = _usuarioActivo.value ?: return          // RN-05: sin sesión no se registra
        val producto = productoPorId(productoId) ?: return
        if (cantidad <= 0) return
        if (tipo == TipoMovimiento.SALIDA && (motivo == null || cantidad > producto.cantidadDisponible)) return

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val nuevo = Movimiento(
            id = "M${_movimientos.value.size + 1}",
            productoId = productoId,
            fecha = fecha,
            tipo = tipo,
            cantidad = cantidad,
            motivo = if (tipo == TipoMovimiento.SALIDA) motivo else null,
            observacion = observacion,
            responsable = usuario.nombre
        )
        _movimientos.value = _movimientos.value + nuevo

        // RF-07: actualizar la cantidad disponible del producto
        val nuevaCantidad = if (tipo == TipoMovimiento.INGRESO) {
            producto.cantidadDisponible + cantidad
        } else {
            producto.cantidadDisponible - cantidad
        }
        // Decisión del equipo (documentar): AGOTADO se calcula solo según la cantidad.
        val nuevoEstado = when {
            nuevaCantidad == 0 && producto.estado == EstadoProducto.DISPONIBLE -> EstadoProducto.AGOTADO
            nuevaCantidad > 0 && producto.estado == EstadoProducto.AGOTADO -> EstadoProducto.DISPONIBLE
            else -> producto.estado
        }
        _productos.value = _productos.value.map {
            if (it.id == productoId) it.copy(cantidadDisponible = nuevaCantidad, estado = nuevoEstado) else it
        }
    }

    // =====================================================================
    // Módulo "Consulta y reposición"
    // =====================================================================

    /**
     * TODO(equipo): devolver los movimientos del producto indicado, del más reciente al más
     * antiguo (null = todos). RF: "mantener un historial comprensible de ingresos y salidas por
     * producto". Hoy devuelve todos los movimientos, sin filtrar ni ordenar.
     */
    fun historialDe(productoId: String?): List<Movimiento> = _movimientos.value

    /**
     * TODO(equipo): devolver los productos cuya cantidad disponible está bajo su nivel mínimo.
     * RF: "identificar productos cuya cantidad disponible esté bajo su nivel mínimo".
     * Hoy devuelve una lista vacía.
     */
    fun productosBajoMinimo(): List<Producto> = emptyList()
}
