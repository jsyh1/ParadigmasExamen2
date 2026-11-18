package co.edu.poli.Examen2.aplicacion;

import co.edu.poli.Examen2.adaptadores.salida.archivo.CsvExamenArchivoAdapter;
import co.edu.poli.Examen2.adaptadores.salida.memoria.CatalogoExamenesMemoria;
import co.edu.poli.Examen2.aplicacion.eventos.PublicadorExamenes;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.CrearExamenCommand;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.FabricaExamenSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Pruebas de los casos de uso de {@link ExamenService}. */
class ExamenServiceTest {
    @TempDir
    Path directorioTemporal;

    private CatalogoExamenesMemoria catalogo;
    private ExamenService servicio;
    private PublicadorExamenes publicador;

    @BeforeEach
    void configurar() {
        FabricaExamenSangre fabrica = new FabricaExamenSangre();
        catalogo = new CatalogoExamenesMemoria();
        publicador = new PublicadorExamenes();
        servicio = new ExamenService(catalogo,
                new CsvExamenArchivoAdapter(directorioTemporal.resolve("examenes.csv"), fabrica),
                fabrica, publicador);
    }

    @Test
    void registraNotificaYRechazaCodigosDuplicados() {
        AtomicInteger notificaciones = new AtomicInteger();
        publicador.suscribir((examenes, mensaje) -> notificaciones.incrementAndGet());
        CrearExamenCommand comando = comando("EXM-001", "Ana Pérez");

        servicio.registrar(comando);

        assertEquals(1, servicio.listar().size());
        assertEquals(1, notificaciones.get());
        assertThrows(IllegalStateException.class, () -> servicio.registrar(comando));
    }

    @Test
    void serializaYRecuperaCatalogoDesdeCsv() {
        servicio.registrar(comando("EXM-001", "Ana, \"Anita\" Pérez"));
        servicio.serializar();
        catalogo.reemplazarTodos(java.util.Collections.emptyList());

        assertEquals("Ana, \"Anita\" Pérez",
                servicio.deserializar().get(0).getNombrePaciente());
    }

    private CrearExamenCommand comando(String codigo, String nombre) {
        return new CrearExamenCommand(codigo, nombre, "45.25", "O", FactorRh.NEGATIVO);
    }
}
