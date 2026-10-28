# Cappucare Stock — Proyecto base (Sección 001, San Bernardo)

Proyecto Android base para el caso de Vinculación con el Medio **"Control inteligente y oportuno de existencias"** de Cappucare. Es el punto de partida común de todos los equipos de la sección: trae la estructura, las pantallas de cada módulo, la navegación y la persistencia configurada. **La lógica de negocio del caso no está implementada**: esa es la parte que construye cada equipo.

## Avance del equipo (completar antes de entregar)

- **Integrantes:** Absalom Moisés Abarca Venegas · _[nombre del segundo integrante]_
- **Repositorio GitHub:** _[pegar enlace]_
- **Implementado hasta ahora:** RF-04 (registrar ingreso), RF-05 (registrar salida, incluidas ventas), RF-06 (responsable del movimiento) y RF-07 (actualización automática de la cantidad disponible), con las reglas RN-01, RN-02, RN-03 y RN-05 aplicadas en `CappucareViewModel.validarMovimiento()` y `registrarMovimiento()`.
- **Pendiente:** login real, RF-01 a RF-03, RF-08 a RF-10, persistencia con Room, recurso nativo (con su justificación) y animaciones.
- **Recurso nativo usado y justificación:** _[completar cuando se implemente]_

Documentación del caso (carpeta `docs/`):

- `Requerimientos_Seccion001.pdf`: requerimientos funcionales, reglas de negocio, RNF y supuestos.
- `Consultas_Pendientes_Seccion001.pdf`: preguntas abiertas al CITT (Paz Morales). No resolver con supuestos propios.

## Cómo abrir y ejecutar

1. Descomprimir el ZIP en una carpeta limpia.
2. Android Studio → **File > Open…** → seleccionar la carpeta que contiene `settings.gradle.kts`.
3. Esperar el Gradle Sync (la primera vez necesita internet).
4. Ejecutar ▶ **Run 'app'**.
5. En el login, usar **"Modo desarrollo"** para entrar (el login real es tarea del equipo).

Si Android Studio sugiere actualizar Gradle o el Android Gradle Plugin, **no aceptar**: las versiones están fijadas para que todos los equipos usen exactamente las mismas.

Usuarios de prueba (en `data/MockData.kt`, para cuando implementen el login):

| Perfil | Usuario | Clave |
|---|---|---|
| Personal encargado | encargado@cappucare.cl | encargado123 |
| Usuario autorizado | ventas@cappucare.cl | ventas123 |

## Stack

Kotlin 2.0 · Jetpack Compose · Material 3 · MVVM (ViewModel + StateFlow) · Navigation Compose · Room + KSP y DataStore (ya configurados en Gradle). El caso no define el stack: se aplica el estándar de la asignatura mientras el CITT no indique otra cosa.

## Flujo de pantallas

```
LOGIN ──(login OK)──> PANEL
PANEL ──> PRODUCTOS ──> DETALLE ──> FORMULARIO (editar)
                    │           ├─> REGISTRAR MOVIMIENTO
                    │           └─> HISTORIAL (de ese producto)
                    └─> FORMULARIO (nuevo, botón +)
PANEL ──> REGISTRAR MOVIMIENTO | ALERTAS DE REPOSICIÓN | HISTORIAL (todos)
Cerrar sesión ──> LOGIN
```

El botón atrás nativo de Android funciona solo (lo maneja el NavHost). La flecha "←" de la barra superior hace lo mismo. Al ingresar, el login sale de la pila; "Cerrar sesión" la limpia completa.

## Estructura del código (`app/src/main/java/com/cappucare/app/`)

| Carpeta | Contenido |
|---|---|
| `model/` | `Producto`, `Movimiento`, `Usuario`, `Perfil` y sus enums (categoría, unidad, estado, tipo, motivo). Incluye `ErroresProducto` y `ErroresMovimiento` para la validación de formularios. |
| `data/` | `MockData.kt`: datos ficticios de ejemplo. |
| `data/local/` | `PersistenciaPendiente.kt`: guía de qué entidades, DAOs y base de datos crear con Room. |
| `viewmodel/` | `CappucareViewModel`: un solo ViewModel compartido. Todas las funciones pendientes están marcadas con `TODO(equipo)`. |
| `navigation/` | `AppNavigation`: rutas, NavHost, ingreso y cierre de sesión. |
| `ui/components/` | `BarraSuperior` (flecha atrás y cerrar sesión) y `SelectorDesplegable` (lista desplegable reutilizable). |
| `ui/screen/` | `LoginScreen`, `PanelScreen` y una carpeta por módulo: `productos/`, `movimientos/`, `reposicion/`. |
| `ui/theme/` y `res/` | Tema, colores, logo e ícono **provisionales**. |

## Trazabilidad: requerimiento → pantalla → función a implementar

| Módulo | Requerimiento | Pantalla (ya construida) | Función en el ViewModel (TODO del equipo) |
|---|---|---|---|
| Acceso | Inicio de sesión del personal | `LoginScreen` | `login()` |
| Gestión de productos | Ingresar producto con código, nombre, descripción, categoría, unidad, nivel mínimo y ubicación | `ProductoFormScreen` | `validarProducto()`, `guardarProducto()` |
| Gestión de productos | Clasificar por categoría | `ProductosScreen` (filtro) | `buscarProductos()` |
| Gestión de productos | Actualizar el estado de un producto | `ProductoDetalleScreen` | `cambiarEstado()` |
| Movimientos | Registrar ingreso (recepción) con cantidad y fecha | `RegistrarMovimientoScreen` | `validarMovimiento()`, `registrarMovimiento()` |
| Movimientos | Registrar salida (incluye ventas) con cantidad, motivo y fecha | `RegistrarMovimientoScreen` | `validarMovimiento()`, `registrarMovimiento()` |
| Movimientos | Registrar el responsable de cada movimiento | `RegistrarMovimientoScreen` | `registrarMovimiento()` (usar `usuarioActivo`) |
| Movimientos | Actualizar automáticamente la cantidad disponible | — | `registrarMovimiento()` |
| Consulta y reposición | Consultar la disponibilidad de cualquier producto | `ProductosScreen`, `ProductoDetalleScreen` | `buscarProductos()` |
| Consulta y reposición | Identificar productos bajo su nivel mínimo | `ReposicionScreen`, `PanelScreen` | `productosBajoMinimo()` |
| Consulta y reposición | Historial de ingresos y salidas por producto | `HistorialMovimientosScreen` | `historialDe()` |
| Persistencia | Guardar los datos en el dispositivo | — | Room: ver `data/local/PersistenciaPendiente.kt` |

**Reglas de negocio que el equipo debe hacer cumplir:**

- La cantidad disponible se calcula como ingresos menos salidas; no se edita a mano (por eso no está en el formulario).
- Una salida por venta se registra con motivo **Venta**.

Para ver todo lo pendiente: **Edit > Find > Find in Files** (Ctrl+Shift+F / Cmd+Shift+F) y buscar `TODO(equipo)`.

## Qué pasa hoy al ejecutar la app (es intencional)

- **Ingresar:** el login siempre rechaza. Hay que entrar con "Modo desarrollo".
- **Formularios:** guardar un producto, cambiar un estado o registrar un movimiento no cambia nada.
- **Filtros:** búsqueda, filtro por categoría e historial por producto muestran todo, sin filtrar.
- **Alertas:** las alertas de reposición aparecen vacías.

## Orden sugerido de trabajo

1. **Login:** validar campos vacíos y luego las credenciales.
2. **Productos:**
   - Validar y guardar el formulario.
   - Hacer funcionar la búsqueda y el filtro por categoría.
   - Implementar el cambio de estado.
3. **Movimientos:** validar, registrar y actualizar la cantidad disponible siguiendo la regla de negocio.
4. **Consulta y reposición:** historial por producto y alertas de productos bajo su nivel mínimo.
5. **Persistencia:** reemplazar `MockData` por Room (Semana 9).
6. **Cierre:**
   - Quitar el "Modo desarrollo".
   - Revisar que no quede ningún `TODO(equipo)`.
   - Reemplazar el logo y los colores provisionales.

Opcional, según alcance y lo que confirme el CITT:

- Escanear códigos QR o de barras con la cámara.
- Notificar cuando un producto quede bajo su nivel mínimo.
- Restringir acciones según el perfil.

## Reglas del proyecto

- **Datos:** solo datos ficticios (exigencia del caso).
- **GitHub:**
  - Un integrante sube esta base como commit inicial.
  - Después, una rama por funcionalidad (por ejemplo `feature/login`).
  - Debe haber commits de **ambos** integrantes.
- **Consultas sobre el caso:** van por el canal Estudiante → Docente → CITT → organización.

## Logo e ícono

Son **provisionales** (un dibujo genérico de caja con cruz). Para usar el logo oficial:

- **Logo:** copiar el PNG a `res/drawable/logo_app.png` y borrar `logo_app.xml`.
- **Ícono de la app:** clic derecho en `res` > **New > Image Asset**.
