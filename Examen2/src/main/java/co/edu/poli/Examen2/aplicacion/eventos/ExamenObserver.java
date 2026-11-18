package co.edu.poli.Examen2.aplicacion.eventos;

import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.util.List;

/**
 * Recibe notificaciones cuando cambia la lista de exámenes o se completa
 * una operación relacionada.
 */
@FunctionalInterface
public interface ExamenObserver {
    /**
     * Procesa una actualización del catálogo.
     *
     * @param examenes instantánea de exámenes vigente
     * @param mensaje descripción de la actualización
     */
    void alActualizar(List<ExamenSangre> examenes, String mensaje);
}
