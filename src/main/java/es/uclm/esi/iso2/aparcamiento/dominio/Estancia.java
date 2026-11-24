package es.uclm.esi.iso2.aparcamiento.dominio;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Estancia en el aparcamiento: ticket de entrada y de salida.
 *
 * Es inmutable y valida sus datos al construirse, de modo que una
 * Estancia existente siempre es correcta. Se admite una salida igual a
 * la entrada (estancia de 0 minutos).
 */
public final class Estancia {

    private final LocalDateTime entrada;
    private final LocalDateTime salida;

    // Lanza FechaNulaException si alguna fecha es nula y
    // SalidaAnteriorAEntradaException si la salida es anterior a la entrada
    public Estancia(LocalDateTime entrada, LocalDateTime salida) {

        if (entrada == null || salida == null) {
            throw new FechaNulaException();
        }

        if (salida.isBefore(entrada)) {
            throw new SalidaAnteriorAEntradaException(entrada, salida);
        }

        this.entrada = entrada;
        this.salida = salida;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    // Minutos de estancia contando como minuto completo el empezado:
    // 30 min 1 s son 31 minutos
    public long minutosEmpezados() {

        Duration duracion = Duration.between(entrada, salida);
        long minutos = duracion.toMinutes();

        if (duracion.compareTo(Duration.ofMinutes(minutos)) > 0) {
            return minutos + 1;
        }

        return minutos;
    }
}
