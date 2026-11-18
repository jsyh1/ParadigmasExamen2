package co.edu.poli.Examen2.dominio;

/**
 * Puerto de dominio para crear exámenes de sangre a partir de datos de entrada.
 */
public interface ExamenFactory {
    /**
     * Valida los datos y construye un examen.
     *
     * @param codigo código del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo expresado como texto
     * @param tipoSangre tipo de sangre expresado como texto
     * @param factorRh factor RH del paciente
     * @return examen creado
     */
    ExamenSangre crear(String codigo, String nombrePaciente, String costo,
                       String tipoSangre, FactorRh factorRh);
}
