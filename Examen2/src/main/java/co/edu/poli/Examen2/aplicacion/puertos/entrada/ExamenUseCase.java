package co.edu.poli.Examen2.aplicacion.puertos.entrada;

import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.nio.file.Path;
import java.util.List;

/**
 * Puerto de entrada que expone las operaciones disponibles para gestionar
 * exámenes de sangre.
 */
public interface ExamenUseCase {
    /**
     * Crea y registra un examen a partir de los datos recibidos.
     *
     * @param comando datos de creación del examen
     * @return examen registrado
     */
    ExamenSangre registrar(CrearExamenCommand comando);

    /**
     * Consulta todos los exámenes registrados.
     *
     * @return exámenes del catálogo
     */
    List<ExamenSangre> listar();

    /**
     * Persiste los exámenes registrados.
     *
     * @return ruta del archivo generado
     */
    Path serializar();

    /**
     * Recupera los exámenes persistidos y actualiza el catálogo.
     *
     * @return exámenes recuperados
     */
    List<ExamenSangre> deserializar();
}
