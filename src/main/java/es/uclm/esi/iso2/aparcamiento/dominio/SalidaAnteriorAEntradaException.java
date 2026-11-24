package es.uclm.esi.iso2.aparcamiento.dominio;

import java.time.LocalDateTime;

/**
 * La fecha de salida es anterior a la de entrada.
 */
public class SalidaAnteriorAEntradaException extends EstanciaInvalidaException {

    public SalidaAnteriorAEntradaException(LocalDateTime entrada,
                                           LocalDateTime salida) {
        super("La salida (" + salida + ") es anterior a la entrada ("
                + entrada + ").");
    }
}
