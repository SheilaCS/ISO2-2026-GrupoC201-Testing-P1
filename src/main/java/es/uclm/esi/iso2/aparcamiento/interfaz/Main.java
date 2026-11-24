package es.uclm.esi.iso2.aparcamiento.interfaz;

import es.uclm.esi.iso2.aparcamiento.dominio.Tarifa;

/**
 * Punto de entrada de la aplicacion de linea de comandos.
 */
public class Main {

    public static void main(String[] args) {

        new AplicacionInterfaz(
                new LectorInterfaz(System.in),
                new EscritorInterfaz(System.out),
                Tarifa.porDefecto()).ejecutar();
    }
}
