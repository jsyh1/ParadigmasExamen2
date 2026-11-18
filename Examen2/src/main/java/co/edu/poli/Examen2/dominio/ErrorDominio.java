package co.edu.poli.Examen2.dominio;

/**
 * Excepción que representa una entrada que infringe una regla del dominio.
 */
public class ErrorDominio extends IllegalArgumentException {
    /**
     * Crea una excepción con el detalle de la regla incumplida.
     *
     * @param mensaje descripción del error de dominio
     */
    public ErrorDominio(String mensaje) {
        super(mensaje);
    }
}
