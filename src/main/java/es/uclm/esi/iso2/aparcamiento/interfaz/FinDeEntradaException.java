package es.uclm.esi.iso2.aparcamiento.interfaz;

/**
 * Se ha terminado la entrada de datos y no hay mas lineas que leer.
 */
public class FinDeEntradaException extends RuntimeException {

    public FinDeEntradaException() {
        super("No hay mas datos de entrada.");
    }
}
