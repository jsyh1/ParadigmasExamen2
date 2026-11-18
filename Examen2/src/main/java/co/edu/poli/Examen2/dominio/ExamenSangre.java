package co.edu.poli.Examen2.dominio;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad inmutable que representa un examen de sangre asociado a un paciente.
 */
public final class ExamenSangre {
    private final String codigo;
    private final String nombrePaciente;
    private final BigDecimal costo;
    private final TipoSangre tipoSangre;
    private final FactorRh factorRh;

    /**
     * Construye un examen con valores ya validados por la fábrica del dominio.
     *
     * @param codigo código del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo del examen
     * @param tipoSangre tipo de sangre del paciente
     * @param factorRh factor RH del paciente
     */
    ExamenSangre(String codigo, String nombrePaciente, BigDecimal costo,
                 TipoSangre tipoSangre, FactorRh factorRh) {
        this.codigo = codigo;
        this.nombrePaciente = nombrePaciente;
        this.costo = costo;
        this.tipoSangre = tipoSangre;
        this.factorRh = factorRh;
    }

    /** @return código del examen */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del paciente */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /** @return costo del examen */
    public BigDecimal getCosto() {
        return costo;
    }

    /** @return tipo de sangre del paciente */
    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    /** @return factor RH del paciente */
    public FactorRh getFactorRh() {
        return factorRh;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof ExamenSangre)) {
            return false;
        }
        ExamenSangre otro = (ExamenSangre) objeto;
        return codigo.equals(otro.codigo)
                && nombrePaciente.equals(otro.nombrePaciente)
                && costo.compareTo(otro.costo) == 0
                && tipoSangre == otro.tipoSangre
                && factorRh == otro.factorRh;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nombrePaciente, costo.stripTrailingZeros(),
                tipoSangre, factorRh);
    }
}
