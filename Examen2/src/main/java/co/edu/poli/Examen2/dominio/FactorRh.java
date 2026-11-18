package co.edu.poli.Examen2.dominio;

/**
 * Factores RH admitidos para un paciente.
 */
public enum FactorRh {
    /** Factor RH positivo. */
    POSITIVO("Positivo"),
    /** Factor RH negativo. */
    NEGATIVO("Negativo");

    private final String descripcion;

    FactorRh(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la etiqueta legible del factor RH.
     *
     * @return descripción localizada del valor
     */
    public String getDescripcion() {
        return descripcion;
    }
}
