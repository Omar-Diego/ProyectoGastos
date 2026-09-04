# ProyectoFinanzasPersonales

Aplicacion movil Android para el control de finanzas personales. Permite registrar ingresos y gastos, visualizar un resumen mensual con graficas, y consultar el historial completo de movimientos.

---

## Indice

- [Caracteristicas](#caracteristicas)
- [Tecnologias](#tecnologias)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Instalacion y configuracion](#instalacion-y-configuracion)
- [Arquitectura](#arquitectura)
- [Modelo de datos](#modelo-de-datos)
- [Pantallas y navegacion](#pantallas-y-navegacion)
- [Reglas de negocio](#reglas-de-negocio)
- [Diseno](#diseno)
- [Testing](#testing)
- [Convenciones de codigo](#convenciones-de-codigo)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

---

## Caracteristicas

### Resumen
- **Balance total** del mes actual (ingresos - gastos)
- **Variacion porcentual** vs. mes anterior
- **Grafica de barras** de ingresos vs. gastos de los ultimos 6 meses
- **Grafica de dona** con desglose de gastos por categoria
- **Presupuesto mensual** con indicador de disponibilidad

### Registro
- **Teclado numerico** personalizado para captura rapida de montos
- **Seleccion de tipo**: Gasto o Ingreso
- **Seleccion de categoria**: Comida, Transporte, Casa, Ocio, Salud, Salario, Otro
- **Flujo de dos pasos**: Monto -> Categoria

### Historial
- **Lista cronologica** de todos los movimientos
- **Filtros**: Todos, Ingresos, Gastos
- **Agrupacion por mes** automatica
- **Eliminacion** con confirmacion
- **Estados vacios** contextuales segun el filtro

---

## Tecnologias

| Componente | Tecnologia |
|------------|------------|
| **Lenguaje** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Arquitectura** | MVVM (Model-View-ViewModel) |
| **Base de datos** | Room |
| **Navegacion** | Navigation Compose |
| **Gestion de estado** | StateFlow + Compose State |
| **Coroutines** | Kotlin Coroutines |
| **DI** | Inyeccion manual (sin framework) |
| **Minimum SDK** | API 26 (Android 8.0) |
| **Target SDK** | API 37 |

---

## Estructura del proyecto

```
app/src/main/java/mx/proyecto/gastos/
├── MainActivity.kt                    # Punto de entrada de la app
├── core/
│   ├── db/                            # Capa de persistencia (Room)
│   │   ├── AppDatabase.kt            # Configuracion de la base de datos
│   │   ├── Converters.kt             # Conversores de tipos para Room
│   │   ├── Mappers.kt                # Mappers entre Entity y Modelo
│   │   ├── MovimientoDao.kt          # DAO con queries de acceso a datos
│   │   └── MovimientoEntity.kt       # Entidad de Room
│   ├── modelo/                        # Modelos de dominio
│   │   ├── Categoria.kt              # Enum de categorias
│   │   ├── Movimiento.kt             # Modelo de negocio principal
│   │   └── TipoMovimiento.kt         # Enum: GASTO, INGRESO
│   └── repo/                          # Repositorio (capa de acceso a datos)
│       ├── MovimientoRepository.kt    # Interface del contrato
│       ├── MovimientoRepositoryRoom.kt # Implementacion con Room
│       └── RepositorioDePrueba.kt     # Repositorio de prueba (testing)
├── historial/                          # Modulo de Historial
│   ├── HistorialFiltros.kt           # Filtros: Todos, Ingresos, Gastos
│   ├── HistorialScreen.kt            # Pantalla principal de historial
│   ├── HistorialViewModel.kt         # ViewModel del historial
│   └── TransaccionItem.kt            # Componente de cada transaccion
├── nav/                                # Navegacion
│   └── NavGraph.kt                   # Grafo de navegacion y barra inferior
├── registro/                           # Modulo de Registro
│   ├── Dinero.kt                     # Componente de visualizacion de monto
│   ├── PasoCategoria.kt              # Paso 2: Seleccion de categoria
│   ├── PasoMonto.kt                  # Paso 1: Captura de monto
│   ├── RegistroScreen.kt             # Pantalla principal de registro
│   ├── RegistroViewModel.kt          # ViewModel del registro
│   └── TecladoNumerico.kt            # Teclado numerico personalizado
├── resumen/                            # Modulo de Resumen
│   ├── CalculadoraResumen.kt         # Logica de calculo del resumen
│   ├── GraficasResumen.kt            # Graficas: barras, dona, leyendas
│   ├── PresupuestoMensual.kt         # Configuracion de presupuesto
│   ├── ResumenMes.kt                 # Modelos de datos del resumen
│   ├── ResumenScreen.kt              # Pantalla principal de resumen
│   └── ResumenViewModel.kt           # ViewModel del resumen
└── ui/                                 # Componentes compartidos
    ├── components/
    │   └── EmptyState.kt             # Componente de estado vacio
    └── theme/
        ├── Color.kt                  # Definicion de colores
        ├── Iconos.kt                 # Iconos personalizados
        ├── Theme.kt                  # Tema de Material 3
        └── Type.kt                   # Tipografia
```

---

## Instalacion y configuracion

### Prerrequisitos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11** o superior
- **Dispositivo o emulador** con Android 8.0 (API 26) o superior

### Pasos

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Omar-Diego/ProyectoGastos.git
   cd ProyectoGastos
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an Existing Project"
   - Navegar hasta la carpeta clonada

3. **Sincronizar dependencias**
   - Android Studio sincronizara automaticamente Gradle
   - Si no, hacer clic en "Sync Now" en la barra de notificaciones

4. **Ejecutar la app**
   - Seleccionar un dispositivo o emulador
   - Hacer clic en el boton Run
   - O usar el atajo: `Shift + F10`

### Build sin Android Studio

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Ejecutar tests
./gradlew test
```

---

## Arquitectura

La aplicacion sigue el patron **MVVM (Model-View-ViewModel)** con una arquitectura en capas:

```
+-----------------------------------------------------+
|                    UI (Compose)                      |
|  +---------+  +---------+  +---------+             |
|  | Resumen |  |Registro |  |Historial|             |
|  +----+----+  +----+----+  +----+----+             |
|       |            |            |                    |
+-------+------------+------------+--------------------+
|       |      ViewModel         |                    |
|  +----+----+  +----+----+  +---+-----+             |
|  | Resumen |  |Registro |  |Historial|             |
|  |   VM    |  |   VM    |  |   VM    |             |
|  +----+----+  +----+----+  +----+----+             |
|       |            |            |                    |
+-------+------------+------------+--------------------+
|       |        Repositorio      |                    |
|       |    +---------------+    |                    |
|       +--->|  Movimiento   |<---+                   |
|            |  Repository   |                        |
|            +-------+-------+                        |
|                    |                                |
+--------------------+--------------------------------+
|              Persistencia                           |
|            +-------+-------+                        |
|            |  Room (SQLite)|                        |
|            +---------------+                        |
+-----------------------------------------------------+
```

### Principios clave

- **Separacion de responsabilidades**: Cada modulo (registro, resumen, historial) gestiona su propia UI, ViewModel y logica
- **Repository Pattern**: Los ViewModels nunca acceden directamente a la base de datos; usan la interface `MovimientoRepository`
- **Reactividad**: Room emite `Flow<List<Movimiento>>` que actualiza automaticamente la UI cuando los datos cambian
- **Inyeccion manual**: Se usa inyeccion de dependencias manual en `MainActivity` para mayor simplicidad

---

## Modelo de datos

### Movimiento

La entidad principal del sistema:

```kotlin
data class Movimiento(
    val id: Long = 0L,              // 0 = aun no guardado
    val montoCentavos: Long,        // Monto en centavos (entero)
    val tipo: TipoMovimiento,       // GASTO o INGRESO
    val categoria: Categoria,       // Una de las 7 categorias
    val fecha: LocalDate,           // Fecha contable (sin hora)
    val nota: String? = null        // Nota opcional
)
```

### TipoMovimiento

```kotlin
enum class TipoMovimiento {
    GASTO,
    INGRESO
}
```

### Categoria

```kotlin
enum class Categoria(val etiqueta: String) {
    COMIDA(etiqueta = "Comida"),
    TRANSPORTE(etiqueta = "Transporte"),
    CASA(etiqueta = "Casa"),
    OCIO(etiqueta = "Ocio"),
    SALUD(etiqueta = "Salud"),
    SALARIO(etiqueta = "Salario"),
    OTRO(etiqueta = "Otro")
}
```

### Presupuesto mensual

```kotlin
object PresupuestoMensual {
    const val CENTAVOS: Long = 1_000_000L  // $10,000.00 MXN
}
```

---

## Pantallas y navegacion

### Navegacion

La app utiliza **Navigation Compose** con una barra de navegacion inferior de 3 destinos:

| Destino | Ruta | Descripcion |
|---------|------|-------------|
| Resumen | `resumen` | Dashboard principal con graficas |
| Registro | `registro` | Captura de nuevos movimientos |
| Historial | `historial` | Lista de todos los movimientos |

### Flujo de Registro

```
+-------------+    +-------------+
|  Paso 1:    |    |  Paso 2:    |
|  Monto      |--->|  Categoria  |---> Guardar
|  (Teclado)  |    |  (Seleccion)|
+-------------+    +-------------+
```

1. El usuario captura el monto usando el teclado numerico
2. Selecciona si es Gasto o Ingreso
3. Elige la categoria correspondiente
4. El movimiento se guarda y se redirige al Resumen

### Pantalla de Resumen

- **Tarjeta de Balance**: Muestra el balance total, ingresos, gastos del mes
- **Grafica de Historial**: Barras de ingresos vs. gastos de los ultimos 6 meses
- **Tarjeta de Categorias**: Dona con desglose porcentual de gastos

### Pantalla de Historial

- **Filtros**: Todos, Ingresos, Gastos
- **Lista agrupada por mes**: Cada mes como encabezado
- **Eliminacion**: Long press para eliminar con confirmacion
- **Estados vacios**: Mensajes contextuales cuando no hay datos

---

## Reglas de negocio

| ID | Regla |
|----|-------|
| RN-1 | Los montos se almacenan como enteros en centavos (nunca decimales de punto flotante) |
| RN-2 | El monto maximo permitido es $999,999.99 MXN |
| RN-3 | Cada movimiento es de tipo GASTO o INGRESO |
| RN-4 | La fecha es contable (solo dia, sin hora) y se asigna automaticamente |
| RN-5 | Existen 6 categorias fijas para la primera entrega |
| RN-6 | El presupuesto mensual es configurable en `PresupuestoMensual` |

---

## Diseno

### Paleta de colores

| Color | Codigo | Uso |
|-------|--------|-----|
| Azul Principal | `#053394` | Header, botones principales |
| Azul Claro | `#2592E8` | Acentos, indicadores |
| Verde | `#0EAF70` | Ingresos, positivo |
| Rojo | `#C42830` | Gastos, negativo, errores |
| Texto | `#1E1E1E` | Texto principal |
| Fondo | `#FFFFFF` | Fondo de pantalla |

### Categorias y colores

| Categoria | Color |
|-----------|-------|
| Comida | `#EEEC7D` (Amarillo) |
| Transporte | `#76B5EB` (Azul claro) |
| Casa | `#78EAE3` (Turquesa) |
| Ocio | `#F897D5` (Rosa) |
| Salud | `#EB5E5A` (Rojo claro) |
| Salario | `#66B47B` (Verde) |
| Otro | `#B5EA78` (Verde claro) |

### Tipografia

Se utiliza la tipografia por defecto de Material 3 con las siguientes jerarquias:

- **displayLarge**: Montos principales
- **titleLarge**: Encabezados de pantalla
- **titleMedium**: Titulos de tarjetas
- **bodyMedium**: Texto descriptivo
- **bodySmall**: Texto secundario
- **labelMedium**: Etiquetas y tags

---

## Testing

### Tests unitarios

```bash
./gradlew test
```

### Tests instrumentados

```bash
./gradlew connectedAndroidTest
```

### Repositorio de prueba

El proyecto incluye `RepositorioDePrueba` para testing sin base de datos:

```kotlin
// En tu test o preview
val repositorio = RepositorioDePrueba()
```

---

## Convenciones de codigo

- **Lenguaje**: Kotlin
- **Naming**: snake_case para archivos, camelCase para variables/funciones
- **Commits**: [Conventional Commits](https://www.conventionalcommits.org/) en espanol
  - `feat(modulo): descripcion`
  - `fix(modulo): descripcion`
  - `chore: descripcion`
- **Arquitectura**: MVVM con Repository Pattern
- **UI**: Jetpack Compose con Material 3

---

## Contribuir

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feat/nueva-feature`)
3. Hacer commit de tus cambios (`git commit -m 'feat(modulo): agregar nueva feature'`)
4. Push a la rama (`git push origin feat/nueva-feature`)
5. Abrir un Pull Request

### Guia de commits

```
feat(registro): agregar validacion de monto minimo
fix(resumen): corregir calculo de variacion porcentual
chore: actualizar dependencias de Gradle
docs: agregar documentacion de arquitectura
```

---

## Licencia

Este proyecto es de uso educativo. Todos los derechos reservados.

---

## Equipo

- **Omar Diego** - Desarrollador principal

---

## Soporte

Si tienes problemas o preguntas, abre un issue en el repositorio de GitHub.
