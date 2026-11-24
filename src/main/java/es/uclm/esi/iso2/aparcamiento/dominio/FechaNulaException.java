package es.uclm.esi.iso2.aparcamiento.dominio;

/**
 * Se ha recibido una fecha de entrada o de salida nula.
 */
public class FechaNulaException extends EstanciaInvalidaException {

    public FechaNulaException() {
        super("La fecha de entrada y la de salida son obligatorias.");
    }
}
