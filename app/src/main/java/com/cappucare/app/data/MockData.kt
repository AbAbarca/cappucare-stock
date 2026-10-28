package com.cappucare.app.data

import com.cappucare.app.model.CategoriaInsumo
import com.cappucare.app.model.EstadoProducto
import com.cappucare.app.model.MotivoSalida
import com.cappucare.app.model.Movimiento
import com.cappucare.app.model.Perfil
import com.cappucare.app.model.Producto
import com.cappucare.app.model.TipoMovimiento
import com.cappucare.app.model.UnidadMedida
import com.cappucare.app.model.Usuario

/**
 * Datos FICTICIOS de ejemplo (el caso exige no usar datos reales de la organización, clientes
 * ni trabajadores). Sirven para que las pantallas tengan algo que mostrar mientras el equipo
 * implementa la persistencia real con Room.
 *
 * Nota: aquí cantidadDisponible viene "escrita a mano" solo para tener datos iniciales. En la
 * app real se calcula a partir de los movimientos (regla de negocio del caso).
 */
object MockData {

    /** Usuarios de prueba para el login. CappucareViewModel.login() todavía NO los usa. */
    val usuarios = listOf(
        Usuario("U1", "Encargada de bodega (demo)", "encargado@cappucare.cl", "encargado123", Perfil.ENCARGADO),
        Usuario("U2", "Vendedor autorizado (demo)", "ventas@cappucare.cl", "ventas123", Perfil.USUARIO_AUTORIZADO),
    )

    val productos = listOf(
        Producto("P1", "CUR-001", "Gasa estéril 10x10 cm", "Sobre de gasa estéril para curaciones", CategoriaInsumo.CURACION, UnidadMedida.PAQUETE, 40, 20, "Estante A1"),
        Producto("P2", "CUR-002", "Venda elástica 10 cm", "Venda elástica de uso general", CategoriaInsumo.CURACION, UnidadMedida.UNIDAD, 8, 15, "Estante A2"),
        Producto("P3", "PRO-001", "Guantes de nitrilo talla M", "Caja de 100 guantes desechables", CategoriaInsumo.PROTECCION, UnidadMedida.CAJA, 12, 10, "Estante B1"),
        Producto("P4", "PRO-002", "Mascarilla quirúrgica", "Caja de 50 mascarillas de tres pliegues", CategoriaInsumo.PROTECCION, UnidadMedida.CAJA, 3, 10, "Estante B1"),
        Producto("P5", "MED-001", "Termómetro digital", "Termómetro digital de uso clínico", CategoriaInsumo.MEDICION, UnidadMedida.UNIDAD, 6, 5, "Vitrina C1"),
        Producto("P6", "MED-002", "Oxímetro de pulso", "Oxímetro de dedo con pantalla", CategoriaInsumo.MEDICION, UnidadMedida.UNIDAD, 0, 3, "Vitrina C1", EstadoProducto.AGOTADO),
        Producto("P7", "HIG-001", "Alcohol gel 500 ml", "Alcohol gel al 70%", CategoriaInsumo.HIGIENE, UnidadMedida.FRASCO, 25, 12, "Estante D2"),
        Producto("P8", "ORT-001", "Cuello ortopédico blando", "Talla única", CategoriaInsumo.ORTOPEDIA, UnidadMedida.UNIDAD, 4, 2, "Estante E1"),
    )

    val movimientos = listOf(
        Movimiento("M1", "P1", "2026-10-01 09:15", TipoMovimiento.INGRESO, 50, responsable = "Encargada de bodega (demo)"),
        Movimiento("M2", "P1", "2026-10-02 16:40", TipoMovimiento.SALIDA, 10, MotivoSalida.VENTA, responsable = "Vendedor autorizado (demo)"),
        Movimiento("M3", "P4", "2026-10-03 11:05", TipoMovimiento.SALIDA, 7, MotivoSalida.VENTA, responsable = "Vendedor autorizado (demo)"),
        Movimiento("M4", "P6", "2026-10-04 10:30", TipoMovimiento.SALIDA, 1, MotivoSalida.MERMA, "Pantalla dañada", "Encargada de bodega (demo)"),
    )
}
