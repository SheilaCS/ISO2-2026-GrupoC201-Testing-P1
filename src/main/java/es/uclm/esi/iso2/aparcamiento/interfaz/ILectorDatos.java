package es.uclm.esi.iso2.aparcamiento.interfaz;

/**
 * Entrada de datos de la aplicacion (por ejemplo, el teclado).
 */
public interface ILectorDatos {

    // Lee una linea de texto. Lanza FinDeEntradaException si ya no hay mas
    String leerLinea();
}
