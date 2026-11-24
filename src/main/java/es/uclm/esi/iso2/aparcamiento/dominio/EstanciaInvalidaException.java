package es.uclm.esi.iso2.aparcamiento.dominio;

/**
 * Error base cuando los datos de una estancia no son validos.
 * Es una excepcion no comprobada: se produce por datos incorrectos.
 */
public class EstanciaInvalidaException extends RuntimeException {

    public EstanciaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
