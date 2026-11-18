package co.edu.poli.Examen2.aplicacion.puertos.entrada;

import co.edu.poli.Examen2.dominio.FactorRh;

/**
 * Contiene los datos de entrada necesarios para solicitar la creación de
 * un examen de sangre.
 */
public final class CrearExamenCommand {
    private final String codigo;
    private final String nombrePaciente;
    private final String costo;
    private final String tipoSangre;
    private final FactorRh factorRh;

    /**
     * Crea un comando con los datos proporcionados por el usuario.
     *
     * @param codigo código del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo expresado como texto para su validación
     * @param tipoSangre tipo de sangre expresado como texto
     * @param factorRh factor RH del paciente
     */
    public CrearExamenCommand(String codigo, String nombrePaciente, String costo,
                              String tipoSangre, FactorRh factorRh) {
        this.codigo = codigo;
        this.nombrePaciente = nombrePaciente;
        this.costo = costo;
        this.tipoSangre = tipoSangre;
        this.factorRh = factorRh;
    }

    /** @return código solicitado para el examen */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del paciente */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /** @return costo proporcionado como texto */
    public String getCosto() {
        return costo;
    }

    /** @return tipo de sangre proporcionado como texto */
    public String getTipoSangre() {
        return tipoSangre;
    }

    /** @return factor RH seleccionado */
    public FactorRh getFactorRh() {
        return factorRh;
    }
}
