package co.edu.poli.Examen2.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Pruebas de validación y normalización de {@link FabricaExamenSangre}. */
class FabricaExamenSangreTest {
    private final FabricaExamenSangre fabrica = new FabricaExamenSangre();

    @Test
    void creaExamenNormalizandoCodigoYCosto() {
        ExamenSangre examen = fabrica.crear(" exm-001 ", "Ana Pérez", "150,5", "ab",
                FactorRh.POSITIVO);

        assertEquals("EXM-001", examen.getCodigo());
        assertEquals(new BigDecimal("150.50"), examen.getCosto());
        assertEquals(TipoSangre.AB, examen.getTipoSangre());
    }

    @Test
    void rechazaCostoCeroONoNumerico() {
        assertThrows(ErrorDominio.class,
                () -> fabrica.crear("EXM-001", "Ana", "0", "A", FactorRh.NEGATIVO));
        assertThrows(ErrorDominio.class,
                () -> fabrica.crear("EXM-001", "Ana", "gratis", "A", FactorRh.NEGATIVO));
    }

    @Test
    void rechazaNombreConSaltoDeLinea() {
        assertThrows(ErrorDominio.class,
                () -> fabrica.crear("EXM-001", "Ana\nPérez", "10", "O", FactorRh.POSITIVO));
    }
}
