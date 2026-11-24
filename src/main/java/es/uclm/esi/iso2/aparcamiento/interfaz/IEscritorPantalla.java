package es.uclm.esi.iso2.aparcamiento.interfaz;

/**
 * Salida de datos de la aplicacion (por ejemplo, la pantalla).
 */
public interface IEscritorPantalla {

    // Muestra un mensaje normal
    void mostrar(String texto);

    // Muestra un mensaje de error
    void mostrarError(String texto);
}
