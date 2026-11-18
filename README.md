# Exámenes de Sangre

Aplicación de escritorio para registrar y consultar exámenes de sangre. La solución separa la interfaz JavaFX del dominio y los casos de uso mediante puertos y adaptadores.

## Requisitos

- JDK 11 o superior
- Maven 3.8 o superior

## Ejecutar y probar

Desde la carpeta `Examen2`:

```powershell
mvn clean javafx:run
mvn test
```

Los registros se mantienen en memoria durante la sesión. El botón **Serializar** guarda una copia CSV en `.examenes-sangre/examenes.csv`, dentro de la carpeta de usuario; **Deserializar** reemplaza el catálogo en memoria con el contenido del archivo. El archivo contiene nombres y datos de salud en texto sin cifrar: úsese solo con datos de prueba y no como almacenamiento clínico de producción.

## Estructura

- `dominio`: entidad `ExamenSangre`, tipos sanguíneos, factor RH y fábrica de entidades.
- `aplicacion`: casos de uso y puertos de entrada/salida.
- `adaptadores.entrada.javafx`: controlador que traduce eventos de pantalla a casos de uso.
- `adaptadores.salida.memoria`: catálogo durante la sesión.
- `adaptadores.salida.archivo`: adaptador CSV para serializar y recuperar el catálogo.
- `vista`: punto de composición e inicio de JavaFX.

## Patrones GoF

- **Factory Method (creacional):** `ExamenFactory` / `FabricaExamenSangre` centralizan la validación y construcción del dominio.
- **Adapter (estructural):** `CsvExamenArchivoAdapter` adapta el archivo CSV al puerto `ArchivoExamenPort`.
- **Observer (comportamiento):** `PublicadorExamenes` notifica cambios al controlador sin acoplar los casos de uso a JavaFX.

## Entregables de la actividad

Los informes y el diagrama de clases actualizado están en la raíz del repositorio: `actividad1.pdf`, `actividad3.pdf` y `dominio3.png`. La fuente PlantUML editable y su versión SVG están en `docs/diagramas/`; el diagrama presenta el dominio, la aplicación y sus puertos, sin vistas ni adaptadores concretos.

Para cumplir el requisito de publicación, copia estos artefactos a la Wiki del fork cuando este se haya creado.

## Uso de IA

Se utilizó **GitHub Copilot (asistente de IA mediante Copilot SDK en VS Code)** para inspeccionar la estructura del proyecto, apoyar el diagnóstico, proponer la separación hexagonal y los patrones GoF, redactar/refactorizar código, diseñar pruebas y preparar la documentación y los diagramas. Las decisiones y los resultados de compilación deben revisarse antes de utilizar el proyecto fuera del contexto académico.

## Estado del fork

Este directorio es un clon local del repositorio base. No se creó un fork remoto, no se configuró un remoto personal y no se realizaron commits.
