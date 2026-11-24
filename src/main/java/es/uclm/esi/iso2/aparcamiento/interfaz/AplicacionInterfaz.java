package es.uclm.esi.iso2.aparcamiento.interfaz;

import es.uclm.esi.iso2.aparcamiento.dominio.EstanciaInvalidaException;
import es.uclm.esi.iso2.aparcamiento.dominio.Tarifa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Flujo de la aplicacion de linea de comandos: pide la entrada y la salida,
 * calcula el coste con la tarifa y lo muestra.
 *
 * Solo usa las interfaces de lectura y escritura, y el dominio no sabe
 * nada de la interfaz.
 */
public class AplicacionInterfaz {

    // Formato de las fechas que escribe el usuario (STRICT rechaza 31/04)
    private static final DateTimeFormatter FORMATO = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final String FORMATO_TEXTO = "dd/MM/aaaa HH:mm";

    private final ILectorDatos lector;
    private final IEscritorPantalla escritor;
    private final Tarifa tarifa;

    public AplicacionInterfaz(ILectorDatos lector,
                             IEscritorPantalla escritor,
                             Tarifa tarifa) {
        this.lector = Objects.requireNonNull(lector);
        this.escritor = Objects.requireNonNull(escritor);
        this.tarifa = Objects.requireNonNull(tarifa);
    }

    // Calcula estancias hasta que el usuario diga que no o se acabe la entrada
    public void ejecutar() {

        escritor.mostrar("Coste de una estancia en el aparcamiento");

        try {
            boolean otra;

            do {
                calcularUnaEstancia();
                otra = preguntarOtra();
            } while (otra);

        } catch (FinDeEntradaException e) {
            escritor.mostrar("Fin de la entrada de datos.");
        }

        escritor.mostrar("Hasta pronto.");
    }

    private void calcularUnaEstancia() {

        LocalDateTime entrada = pedirFechaHora("Entrada");
        LocalDateTime salida = pedirFechaHora("Salida");

        try {
            BigDecimal coste = tarifa.calcularCoste(entrada, salida);
            escritor.mostrar("Coste de la estancia: " + formatear(coste));

        } catch (EstanciaInvalidaException e) {
            escritor.mostrarError(e.getMessage());
        }
    }

    // Repite la pregunta hasta que la fecha tiene el formato correcto
    private LocalDateTime pedirFechaHora(String etiqueta) {

        while (true) {
            escritor.mostrar(etiqueta + " (" + FORMATO_TEXTO + "):");

            try {
                return LocalDateTime.parse(lector.leerLinea().trim(), FORMATO);

            } catch (DateTimeParseException e) {
                escritor.mostrarError("Fecha y hora incorrectas. Use el formato "
                        + FORMATO_TEXTO + " y una fecha que exista.");
            }
        }
    }

    // Repite la pregunta hasta recibir s o n
    private boolean preguntarOtra() {

        while (true) {
            escritor.mostrar("Calcular otra estancia? (s/n)");

            String respuesta = lector.leerLinea().trim();

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            escritor.mostrarError("Responda s o n.");
        }
    }

    // Importe con 2 decimales y coma decimal: 1,50 EUR
    private static String formatear(BigDecimal importe) {
        return String.format(Locale.forLanguageTag("es-ES"),
                "%.2f EUR", importe);
    }
}
