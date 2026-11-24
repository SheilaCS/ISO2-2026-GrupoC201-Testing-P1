package es.uclm.esi.iso2.aparcamiento.interfaz;

import java.io.PrintStream;

/**
 * Escribe en la pantalla (o en cualquier PrintStream).
 */
public class EscritorInterfaz implements IEscritorPantalla {

    private final PrintStream salida;

    public EscritorInterfaz(PrintStream salida) {
        this.salida = salida;
    }

    @Override
    public void mostrar(String texto) {
        salida.println(texto);
    }

    @Override
    public void mostrarError(String texto) {
        salida.println("Error: " + texto);
    }
}
