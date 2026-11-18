package co.edu.poli.Examen2.adaptadores.salida.memoria;

import co.edu.poli.Examen2.aplicacion.puertos.salida.ExamenCatalogoPort;
import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación en memoria del catálogo de exámenes, conservando el orden
 * en que fueron registrados.
 */
public final class CatalogoExamenesMemoria implements ExamenCatalogoPort {
    private final Map<String, ExamenSangre> examenes = new LinkedHashMap<>();

    /**
     * Devuelve una instantánea no modificable de los exámenes registrados.
     *
     * @return exámenes en su orden de registro
     */
    @Override
    public List<ExamenSangre> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(examenes.values()));
    }

    /**
     * Agrega un examen al catálogo.
     *
     * @param examen examen que se agregará
     * @throws IllegalStateException si ya existe un examen con el mismo código
     */
    @Override
    public void guardar(ExamenSangre examen) {
        if (examenes.containsKey(examen.getCodigo())) {
            throw new IllegalStateException("Ya existe un examen con ese código.");
        }
        examenes.put(examen.getCodigo(), examen);
    }

    /**
     * Reemplaza el catálogo con los exámenes recibidos de forma atómica.
     *
     * @param nuevosExamenes exámenes que conformarán el catálogo
     * @throws IllegalArgumentException si la lista contiene códigos duplicados
     */
    @Override
    public void reemplazarTodos(List<ExamenSangre> nuevosExamenes) {
        Map<String, ExamenSangre> reemplazo = new LinkedHashMap<>();
        for (ExamenSangre examen : nuevosExamenes) {
            if (reemplazo.putIfAbsent(examen.getCodigo(), examen) != null) {
                throw new IllegalArgumentException("No se puede cargar un catálogo con códigos duplicados.");
            }
        }
        examenes.clear();
        examenes.putAll(reemplazo);
    }
}
