package es.uclm.esi.iso2.aparcamiento.dominio;

/**
 * Los parametros de la tarifa no son validos.
 */
public class TarifaInvalidaException extends RuntimeException {

    public TarifaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
