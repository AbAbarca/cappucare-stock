package com.cappucare.app.data.local

// =====================================================================================
// TODO(equipo): PERSISTENCIA LOCAL (contenido de la Semana 9)
// =====================================================================================
// Las dependencias de Room, KSP y DataStore YA están configuradas en Gradle. Lo que falta
// es crear las clases en esta carpeta (data/local/) y reemplazar las listas de MockData
// que hoy usa CappucareViewModel.
//
// 1) ENTIDADES (@Entity) — una por tabla del Modelo Relacional del equipo. Mínimo:
//      - ProductoEntity     (tabla "productos")   -> a partir de model/Producto.kt
//      - MovimientoEntity   (tabla "movimientos") -> a partir de model/Movimiento.kt,
//                                                    con productoId como clave foránea
//    Los enums (CategoriaInsumo, EstadoProducto, etc.) se pueden guardar como String
//    (enum.name) o con un @TypeConverter.
//
// 2) DAOs (@Dao) — consultas que necesita cada pantalla, por ejemplo:
//      - productos ordenados por nombre                     -> Flow<List<ProductoEntity>>
//      - buscar por nombre/código y filtrar por categoría
//      - productos bajo su nivel mínimo                      -> WHERE cantidadDisponible < nivelMinimo
//      - historial de movimientos de un producto, del más reciente al más antiguo
//      - insertar / actualizar producto, insertar movimiento (funciones suspend)
//
// 3) BASE DE DATOS (@Database) — AppDatabase con las entidades, version = 1 y
//    exportSchema = false, y una instancia única (patrón visto en clase).
//
// 4) DATASTORE (opcional) — recordar el último usuario que inició sesión.
//
// 5) VIEWMODEL — reemplazar MutableStateFlow(MockData...) por los Flow del DAO con
//    stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()).
//    Para tener Context, el ViewModel puede extender AndroidViewModel(application).
//
// Si la app se cae después de cambiar una entidad: subir la version de AppDatabase o
// desinstalar la app del emulador (en desarrollo).
// =====================================================================================
