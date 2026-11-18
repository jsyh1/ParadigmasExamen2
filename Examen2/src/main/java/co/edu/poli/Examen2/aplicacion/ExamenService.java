package co.edu.poli.Examen2.aplicacion;

import co.edu.poli.Examen2.aplicacion.eventos.PublicadorExamenes;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.CrearExamenCommand;
import co.edu.poli.Examen2.aplicacion.puertos.entrada.ExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puertos.salida.ArchivoExamenPort;
import co.edu.poli.Examen2.aplicacion.puertos.salida.ExamenCatalogoPort;
import co.edu.poli.Examen2.dominio.ExamenFactory;
import co.edu.poli.Examen2.dominio.ExamenSangre;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementa los casos de uso para registrar, consultar y persistir exámenes
 * de sangre, coordinando el catálogo, el archivo y sus observadores.
 */
public final class ExamenService implements ExamenUseCase {
    private final ExamenCatalogoPort catalogo;
    private final ArchivoExamenPort archivo;
    private final ExamenFactory fabrica;
    private final PublicadorExamenes publicador;

    /**
     * Construye el servicio con sus dependencias de entrada y salida.
     *
     * @param catalogo catálogo donde se mantienen los exámenes
     * @param archivo adaptador para guardar y cargar exámenes
     * @param fabrica fábrica que valida y crea exámenes
     * @param publicador componente que notifica los cambios
     */
    public ExamenService(ExamenCatalogoPort catalogo, ArchivoExamenPort archivo,
                         ExamenFactory fabrica, PublicadorExamenes publicador) {
        this.catalogo = catalogo;
        this.archivo = archivo;
        this.fabrica = fabrica;
        this.publicador = publicador;
    }

    /**
     * Valida y registra un examen, y publica el catálogo actualizado.
     *
     * @param comando datos solicitados para crear el examen
     * @return examen creado y registrado
     * @throws IllegalStateException si ya existe un examen con el código indicado
     */
    @Override
    public ExamenSangre registrar(CrearExamenCommand comando) {
        ExamenSangre examen = fabrica.crear(comando.getCodigo(), comando.getNombrePaciente(),
                comando.getCosto(), comando.getTipoSangre(), comando.getFactorRh());
        boolean codigoExiste = catalogo.obtenerTodos().stream()
                .anyMatch(actual -> actual.getCodigo().equals(examen.getCodigo()));
        if (codigoExiste) {
            throw new IllegalStateException("Ya existe un examen con ese código.");
        }
        catalogo.guardar(examen);
        publicador.publicar(catalogo.obtenerTodos(), "Examen registrado correctamente.");
        return examen;
    }

    /**
     * Obtiene los exámenes actualmente registrados.
     *
     * @return exámenes del catálogo
     */
    @Override
    public List<ExamenSangre> listar() {
        return catalogo.obtenerTodos();
    }

    /**
     * Guarda el catálogo actual y notifica la ubicación del archivo.
     *
     * @return ruta del archivo generado
     */
    @Override
    public Path serializar() {
        Path ruta = archivo.guardar(catalogo.obtenerTodos());
        publicador.publicar(catalogo.obtenerTodos(),
                "Exámenes serializados en " + ruta.toAbsolutePath() + ".");
        return ruta;
    }

    /**
     * Carga exámenes desde el archivo, valida que sus códigos sean únicos y
     * reemplaza el catálogo actual.
     *
     * @return exámenes recuperados
     * @throws IllegalArgumentException si hay códigos duplicados en el archivo
     */
    @Override
    public List<ExamenSangre> deserializar() {
        List<ExamenSangre> recuperados = archivo.cargar();
        validarCodigosUnicos(recuperados);
        catalogo.reemplazarTodos(recuperados);
        publicador.publicar(catalogo.obtenerTodos(),
                "Se cargaron " + recuperados.size() + " exámenes desde el archivo.");
        return catalogo.obtenerTodos();
    }

    private void validarCodigosUnicos(List<ExamenSangre> examenes) {
        Set<String> codigos = new HashSet<>();
        for (ExamenSangre examen : examenes) {
            if (!codigos.add(examen.getCodigo())) {
                throw new IllegalArgumentException("El archivo contiene códigos de examen duplicados.");
            }
        }
    }
}
