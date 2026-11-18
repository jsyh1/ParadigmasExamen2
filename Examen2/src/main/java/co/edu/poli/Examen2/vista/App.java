package co.edu.poli.Examen2.vista;

import co.edu.poli.Examen2.adaptadores.entrada.javafx.PrimaryController;
import co.edu.poli.Examen2.adaptadores.salida.archivo.CsvExamenArchivoAdapter;
import co.edu.poli.Examen2.adaptadores.salida.memoria.CatalogoExamenesMemoria;
import co.edu.poli.Examen2.aplicacion.ExamenService;
import co.edu.poli.Examen2.aplicacion.eventos.PublicadorExamenes;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.ExamenUseCase;
import co.edu.poli.Examen2.dominio.ExamenFactory;
import co.edu.poli.Examen2.dominio.FabricaExamenSangre;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Punto de entrada JavaFX que configura los adaptadores, los casos de uso
 * y la vista principal de la aplicación.
 */
public class App extends Application {
    /**
     * Inicializa y muestra la ventana principal.
     *
     * @param stage ventana principal proporcionada por JavaFX
     * @throws IOException si no se encuentra o no se puede cargar la vista FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        ExamenFactory fabrica = new FabricaExamenSangre();
        PublicadorExamenes publicador = new PublicadorExamenes();
        ExamenUseCase casosDeUso = new ExamenService(
                new CatalogoExamenesMemoria(),
                new CsvExamenArchivoAdapter(fabrica),
                fabrica,
                publicador);

        URL recurso = App.class.getResource("/co/edu/poli/Examen2/primary.fxml");
        if (recurso == null) {
            throw new IOException("No se encontró la vista principal de la aplicación.");
        }
        FXMLLoader cargador = new FXMLLoader(recurso);
        cargador.setControllerFactory(tipo -> {
            if (tipo == PrimaryController.class) {
                return new PrimaryController(casosDeUso, publicador);
            }
            throw new IllegalStateException("Controlador FXML no registrado: " + tipo.getName());
        });
        Parent raiz = cargador.load();
        Scene escena = new Scene(raiz, 640, 660);
        stage.setTitle("Exámenes de Sangre");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Inicia el ciclo de vida de la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}