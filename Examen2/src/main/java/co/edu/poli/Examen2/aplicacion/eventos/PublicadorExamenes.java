package co.edu.poli.Examen2.aplicacion.eventos;

import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mantiene los observadores de exámenes y les distribuye instantáneas de las
 * actualizaciones publicadas.
 */
public final class PublicadorExamenes {
    private final List<ExamenObserver> observadores = new ArrayList<>();

    /**
     * Agrega un observador para futuras publicaciones.
     *
     * @param observador receptor de las actualizaciones
     */
    public void suscribir(ExamenObserver observador) {
        observadores.add(observador);
    }

    /**
     * Notifica a los observadores suscritos con una instantánea no modificable.
     *
     * @param examenes exámenes incluidos en la actualización
     * @param mensaje descripción de la actualización
     */
    public void publicar(List<ExamenSangre> examenes, String mensaje) {
        List<ExamenSangre> instantanea = Collections.unmodifiableList(new ArrayList<>(examenes));
        for (ExamenObserver observador : new ArrayList<>(observadores)) {
            observador.alActualizar(instantanea, mensaje);
        }
    }
}
