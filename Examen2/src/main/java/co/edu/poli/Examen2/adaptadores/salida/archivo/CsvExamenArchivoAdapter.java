package co.edu.poli.Examen2.adaptadores.salida.archivo;

import co.edu.poli.Examen2.aplicacion.puertos.salida.ArchivoExamenPort;
import co.edu.poli.Examen2.dominio.ExamenFactory;
import co.edu.poli.Examen2.dominio.ExamenSangre;
import co.edu.poli.Examen2.dominio.FactorRh;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Adaptador que persiste y recupera exámenes de sangre en un archivo CSV UTF-8.
 */
public final class CsvExamenArchivoAdapter implements ArchivoExamenPort {
    private static final String ENCABEZADO = "codigo,nombrePaciente,costo,tipoSangre,factorRh";

    private final Path ruta;
    private final ExamenFactory fabrica;

    /**
     * Crea el adaptador usando la ubicación predeterminada en el directorio
     * personal del usuario.
     *
     * @param fabrica fábrica usada para reconstruir y validar los exámenes
     */
    public CsvExamenArchivoAdapter(ExamenFactory fabrica) {
        this(Path.of(System.getProperty("user.home"), ".examenes-sangre", "examenes.csv"), fabrica);
    }

    /**
     * Crea el adaptador con una ruta de archivo configurable.
     *
     * @param ruta ubicación del archivo CSV
     * @param fabrica fábrica usada para reconstruir y validar los exámenes
     */
    public CsvExamenArchivoAdapter(Path ruta, ExamenFactory fabrica) {
        this.ruta = Objects.requireNonNull(ruta, "ruta");
        this.fabrica = Objects.requireNonNull(fabrica, "fabrica");
    }

    /**
     * Guarda los exámenes en CSV y reemplaza el archivo de forma atómica
     * cuando el sistema de archivos lo permite.
     *
     * @param examenes exámenes que se guardarán
     * @return ruta del archivo guardado
     * @throws UncheckedIOException si no es posible escribir el archivo
     */
    @Override
    public Path guardar(List<ExamenSangre> examenes) {
        Path temporal = null;
        try {
            Path directorio = ruta.toAbsolutePath().getParent();
            Files.createDirectories(directorio);
            temporal = Files.createTempFile(directorio, "examenes-", ".tmp");
            StringBuilder contenido = new StringBuilder(ENCABEZADO).append(System.lineSeparator());
            for (ExamenSangre examen : examenes) {
                contenido.append(campo(examen.getCodigo())).append(',')
                        .append(campo(examen.getNombrePaciente())).append(',')
                        .append(campo(examen.getCosto().toPlainString())).append(',')
                        .append(campo(examen.getTipoSangre().name())).append(',')
                        .append(campo(examen.getFactorRh().name()))
                        .append(System.lineSeparator());
            }
            Files.write(temporal, contenido.toString().getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException excepcion) {
                Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING);
            }
            return ruta;
        } catch (IOException excepcion) {
            if (temporal != null) {
                try {
                    Files.deleteIfExists(temporal);
                } catch (IOException errorLimpieza) {
                    excepcion.addSuppressed(errorLimpieza);
                }
            }
            throw new UncheckedIOException("No fue posible guardar el archivo de exámenes.", excepcion);
        }
    }

    /**
     * Lee el archivo CSV y reconstruye sus exámenes mediante la fábrica.
     *
     * @return exámenes encontrados en el archivo
     * @throws UncheckedIOException si el archivo no se puede leer o su estructura
     *                              CSV no es válida
     * @throws IllegalArgumentException si los datos de una fila no representan
     *                                  un examen válido
     */
    @Override
    public List<ExamenSangre> cargar() {
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            if (lineas.isEmpty() || !ENCABEZADO.equals(lineas.get(0))) {
                throw new IOException("El archivo no contiene el encabezado CSV esperado.");
            }
            List<ExamenSangre> examenes = new ArrayList<>();
            for (int indice = 1; indice < lineas.size(); indice++) {
                List<String> columnas = analizarLinea(lineas.get(indice));
                if (columnas.size() != 5) {
                    throw new IOException("La fila " + (indice + 1) + " no tiene cinco columnas.");
                }
                FactorRh factor = FactorRh.valueOf(columnas.get(4));
                examenes.add(fabrica.crear(columnas.get(0), columnas.get(1), columnas.get(2),
                        columnas.get(3), factor));
            }
            return examenes;
        } catch (IOException excepcion) {
            throw new UncheckedIOException("No fue posible leer el archivo de exámenes.", excepcion);
        } catch (IllegalArgumentException excepcion) {
            throw new IllegalArgumentException("El archivo contiene datos de exámenes no válidos.", excepcion);
        }
    }

    private String campo(String valor) {
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }

    private List<String> analizarLinea(String linea) throws IOException {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;
        boolean cierreComillas = false;

        for (int indice = 0; indice < linea.length(); indice++) {
            char caracter = linea.charAt(indice);
            if (entreComillas) {
                if (caracter == '"' && indice + 1 < linea.length()
                        && linea.charAt(indice + 1) == '"') {
                    campo.append('"');
                    indice++;
                } else if (caracter == '"') {
                    entreComillas = false;
                    cierreComillas = true;
                } else {
                    campo.append(caracter);
                }
            } else if (caracter == ',') {
                campos.add(campo.toString());
                campo.setLength(0);
                cierreComillas = false;
            } else if (caracter == '"' && campo.length() == 0 && !cierreComillas) {
                entreComillas = true;
            } else if (caracter == '"') {
                throw new IOException("La fila contiene comillas CSV mal formadas.");
            } else if (!cierreComillas) {
                campo.append(caracter);
            } else {
                throw new IOException("La fila contiene comillas CSV mal formadas.");
            }
        }
        if (entreComillas) {
            throw new IOException("La fila contiene un campo CSV sin cerrar.");
        }
        campos.add(campo.toString());
        return campos;
    }
}
