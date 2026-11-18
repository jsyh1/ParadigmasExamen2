package co.edu.poli.Examen2.adaptadores.entrada.javafx;

import co.edu.poli.Examen2.aplicacion.eventos.ExamenObserver;
import co.edu.poli.Examen2.aplicacion.eventos.PublicadorExamenes;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.CrearExamenCommand;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.ExamenUseCase;
import co.edu.poli.Examen2.dominio.ExamenSangre;
import co.edu.poli.Examen2.dominio.FactorRh;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controlador JavaFX que adapta las acciones de la vista a los casos de uso
 * de exámenes de sangre y presenta sus resultados.
 */
public final class PrimaryController implements ExamenObserver {
    @FXML
    private TextField txtCodigoExamen;
    @FXML
    private TextField txtNombrePaciente;
    @FXML
    private TextField txtCosto;
    @FXML
    private ComboBox<String> cmbTipoSangre;
    @FXML
    private RadioButton rbPositivo;
    @FXML
    private TextArea txtAreaMensajes;

    private final ExamenUseCase casosDeUso;

    /**
     * Crea el controlador y lo suscribe a las actualizaciones de exámenes.
     *
     * @param casosDeUso operaciones disponibles para gestionar exámenes
     * @param publicador publicador al que se suscribe este controlador
     */
    public PrimaryController(ExamenUseCase casosDeUso, PublicadorExamenes publicador) {
        this.casosDeUso = casosDeUso;
        publicador.suscribir(this);
    }

    @FXML
    private void handleGuardar() {
        ejecutar(() -> {
            FactorRh factor = rbPositivo.isSelected() ? FactorRh.POSITIVO : FactorRh.NEGATIVO;
            CrearExamenCommand comando = new CrearExamenCommand(
                    txtCodigoExamen.getText(),
                    txtNombrePaciente.getText(),
                    txtCosto.getText(),
                    cmbTipoSangre.getValue(),
                    factor);
            casosDeUso.registrar(comando);
            limpiarCampos();
        });
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
        mostrarMensaje("Campos limpiados.");
    }

    @FXML
    private void handleSerializar() {
        ejecutar(casosDeUso::serializar);
    }

    @FXML
    private void handleDeserializar() {
        ejecutar(casosDeUso::deserializar);
    }

    @FXML
    private void handleListar() {
        mostrarLista(casosDeUso.listar(), "Exámenes registrados:");
    }

    /**
     * Actualiza la vista con la lista de exámenes y el mensaje recibido.
     *
     * @param examenes exámenes que se mostrarán
     * @param mensaje encabezado o notificación de la actualización
     */
    @Override
    public void alActualizar(List<ExamenSangre> examenes, String mensaje) {
        mostrarLista(examenes, mensaje);
    }

    private void ejecutar(Runnable operacion) {
        try {
            operacion.run();
        } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException excepcion) {
            mostrarMensaje("Error: " + excepcion.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.getSelectionModel().clearSelection();
        rbPositivo.setSelected(true);
    }

    private void mostrarLista(List<ExamenSangre> examenes, String encabezado) {
        String contenido = examenes.stream()
                .map(examen -> String.format(Locale.ROOT,
                        "%s | %s | $%s | %s %s",
                        examen.getCodigo(), examen.getNombrePaciente(), examen.getCosto().toPlainString(),
                        examen.getTipoSangre(), examen.getFactorRh().getDescripcion()))
                .collect(Collectors.joining(System.lineSeparator()));
        txtAreaMensajes.setText(contenido.isEmpty()
                ? encabezado + System.lineSeparator() + "No hay exámenes registrados."
                : encabezado + System.lineSeparator() + contenido);
    }

    private void mostrarMensaje(String mensaje) {
        txtAreaMensajes.setText(mensaje);
    }
}
