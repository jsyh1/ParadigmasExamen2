package co.edu.poli.Examen2.aplicacion.puertos.salida;

import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.nio.file.Path;
import java.util.List;

/**
 * Puerto de salida para guardar y recuperar exámenes desde un medio persistente.
 */
public interface ArchivoExamenPort {
    /**
     * Persiste la lista de exámenes.
     *
     * @param examenes exámenes que se guardarán
     * @return ruta del archivo utilizado
     */
    Path guardar(List<ExamenSangre> examenes);

    /**
     * Recupera los exámenes persistidos.
     *
     * @return exámenes leídos
     */
    List<ExamenSangre> cargar();
}
