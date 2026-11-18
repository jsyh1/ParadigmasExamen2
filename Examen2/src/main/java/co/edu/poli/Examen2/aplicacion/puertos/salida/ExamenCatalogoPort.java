package co.edu.poli.Examen2.aplicacion.puertos.salida;

import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.util.List;

/**
 * Puerto de salida que define las operaciones de consulta y modificación
 * del catálogo de exámenes.
 */
public interface ExamenCatalogoPort {
    /**
     * Obtiene todos los exámenes del catálogo.
     *
     * @return exámenes registrados
     */
    List<ExamenSangre> obtenerTodos();

    /**
     * Agrega un examen al catálogo.
     *
     * @param examen examen que se agregará
     */
    void guardar(ExamenSangre examen);

    /**
     * Reemplaza el contenido completo del catálogo.
     *
     * @param examenes lista que sustituirá el contenido actual
     */
    void reemplazarTodos(List<ExamenSangre> examenes);
}
