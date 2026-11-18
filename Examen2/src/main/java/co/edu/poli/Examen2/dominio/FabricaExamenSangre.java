package co.edu.poli.Examen2.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Valida y normaliza los datos de entrada para construir exámenes de sangre.
 */
public final class FabricaExamenSangre implements ExamenFactory {
    private static final Pattern FORMATO_CODIGO = Pattern.compile("[A-Z0-9][A-Z0-9-]{0,19}");
    private static final Pattern FORMATO_COSTO = Pattern.compile("\\d+(?:[.,]\\d{1,2})?");

    /**
     * Construye un examen después de validar sus campos y normalizar sus valores.
     *
     * @param codigo código del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo expresado como texto
     * @param tipoSangre tipo de sangre expresado como texto
     * @param factorRh factor RH del paciente
     * @return examen creado con datos normalizados
     * @throws ErrorDominio si algún dato incumple las reglas del dominio
     */
    @Override
    public ExamenSangre crear(String codigo, String nombrePaciente, String costo,
                              String tipoSangre, FactorRh factorRh) {
        String codigoNormalizado = normalizarCodigo(codigo);
        String nombreNormalizado = validarNombre(nombrePaciente);
        BigDecimal costoNormalizado = validarCosto(costo);
        TipoSangre tipoNormalizado = validarTipoSangre(tipoSangre);

        if (factorRh == null) {
            throw new ErrorDominio("Seleccione el factor RH.");
        }

        return new ExamenSangre(codigoNormalizado, nombreNormalizado, costoNormalizado,
                tipoNormalizado, factorRh);
    }

    private String normalizarCodigo(String codigo) {
        if (codigo == null) {
            throw new ErrorDominio("Ingrese el código del examen.");
        }
        String normalizado = codigo.trim().toUpperCase(Locale.ROOT);
        if (!FORMATO_CODIGO.matcher(normalizado).matches()) {
            throw new ErrorDominio("El código debe tener entre 1 y 20 caracteres alfanuméricos o guiones.");
        }
        return normalizado;
    }

    private String validarNombre(String nombrePaciente) {
        if (nombrePaciente == null) {
            throw new ErrorDominio("Ingrese el nombre del paciente.");
        }
        String normalizado = nombrePaciente.trim();
        if (normalizado.isEmpty() || normalizado.length() > 100
                || normalizado.chars().anyMatch(Character::isISOControl)) {
            throw new ErrorDominio("El nombre debe tener entre 1 y 100 caracteres en una sola línea.");
        }
        return normalizado;
    }

    private BigDecimal validarCosto(String costo) {
        if (costo == null) {
            throw new ErrorDominio("Ingrese el costo del examen.");
        }
        String normalizado = costo.trim();
        if (!FORMATO_COSTO.matcher(normalizado).matches()) {
            throw new ErrorDominio("El costo debe ser un número positivo con máximo dos decimales.");
        }
        BigDecimal valor = new BigDecimal(normalizado.replace(',', '.')).setScale(2, RoundingMode.UNNECESSARY);
        if (valor.signum() <= 0) {
            throw new ErrorDominio("El costo debe ser mayor que cero.");
        }
        return valor;
    }

    private TipoSangre validarTipoSangre(String tipoSangre) {
        if (tipoSangre == null || tipoSangre.trim().isEmpty()) {
            throw new ErrorDominio("Seleccione el tipo de sangre.");
        }
        try {
            return TipoSangre.valueOf(tipoSangre.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException excepcion) {
            throw new ErrorDominio("El tipo de sangre seleccionado no es válido.");
        }
    }
}
