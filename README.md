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

## Documentación y diagramas

Los diagramas de arquitectura original, dominio 2, arquitectura 2 y dominio 3, junto con la captura del análisis de SonarQube, están publicados en la [Wiki del proyecto](https://github.com/jsyh1/ParadigmasExamen2/wiki).

Los informes `actividad1.pdf` y `actividad3.pdf` aún deben agregarse cuando estén preparados.

## Uso de IA

Se utilizó **GitHub Copilot (asistente de IA mediante Copilot SDK en VS Code)** para inspeccionar la estructura del proyecto, apoyar el diagnóstico, proponer la separación hexagonal y los patrones GoF, redactar/refactorizar código, diseñar pruebas y preparar la documentación y los diagramas. Las decisiones y los resultados de compilación deben revisarse antes de utilizar el proyecto fuera del contexto académico.

## Repositorio y Wiki

El código está alojado en el fork [jsyh1/ParadigmasExamen2](https://github.com/jsyh1/ParadigmasExamen2). Los diagramas y la captura de SonarQube se mantienen en la Wiki del fork.
