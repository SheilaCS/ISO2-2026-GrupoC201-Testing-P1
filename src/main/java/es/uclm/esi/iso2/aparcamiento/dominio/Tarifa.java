package es.uclm.esi.iso2.aparcamiento.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Tarifa del aparcamiento.
 *
 * Reglas (por defecto): los primeros 30 minutos de la estancia son
 * gratuitos; a partir de ahi se cobra 0,05 EUR por minuto, con un tope
 * de 20 EUR por cada bloque de 24 horas contado desde la entrada.
 * Los minutos gratuitos se aplican una sola vez por estancia.
 *
 * Los importes se calculan con BigDecimal y se devuelven siempre con 2
 * decimales.
 */
public final class Tarifa {

    // Minutos que dura un bloque de cobro con tope (24 horas)
    public static final long MINUTOS_POR_BLOQUE = 24 * 60;

    private static final BigDecimal CERO = new BigDecimal("0.00");

    private final long minutosGratis;
    private final BigDecimal precioPorMinuto;
    private final BigDecimal topeDiario;

    // Tarifa del enunciado: 30 minutos gratis, 0,05 EUR/min, tope 20 EUR
    public static Tarifa porDefecto() {
        return new Tarifa(30, new BigDecimal("0.05"), new BigDecimal("20.00"));
    }

    // Lanza TarifaInvalidaException si algun parametro no es valido
    public Tarifa(long minutosGratis, BigDecimal precioPorMinuto,
                  BigDecimal topeDiario) {

        if (minutosGratis < 0) {
            throw new TarifaInvalidaException(
                    "Los minutos gratuitos no pueden ser negativos.");
        }

        if (precioPorMinuto == null
                || precioPorMinuto.signum() <= 0) {
            throw new TarifaInvalidaException(
                    "El precio por minuto debe ser mayor que cero.");
        }

        if (topeDiario == null || topeDiario.signum() <= 0) {
            throw new TarifaInvalidaException(
                    "El tope diario debe ser mayor que cero.");
        }

        this.minutosGratis = minutosGratis;
        this.precioPorMinuto = precioPorMinuto;
        this.topeDiario = topeDiario;
    }

    public long getMinutosGratis() {
        return minutosGratis;
    }

    public BigDecimal getPrecioPorMinuto() {
        return precioPorMinuto;
    }

    public BigDecimal getTopeDiario() {
        return topeDiario;
    }

    // Coste de la estancia entre dos fechas. Lanza FechaNulaException si
    // alguna es nula y SalidaAnteriorAEntradaException si la salida es
    // anterior a la entrada
    public BigDecimal calcularCoste(LocalDateTime entrada,
                                    LocalDateTime salida) {
        return calcularCoste(new Estancia(entrada, salida));
    }

    // Coste de una estancia ya validada
    public BigDecimal calcularCoste(Estancia estancia) {

        if (estancia == null) {
            throw new EstanciaInvalidaException(
                    "La estancia no puede ser nula.");
        }

        long total = estancia.minutosEmpezados();

        if (total <= minutosGratis) {
            return CERO;
        }

        BigDecimal coste = BigDecimal.ZERO;

        // Cada bloque de 24 h cobra solo los minutos de pago que contiene
        // y nunca mas que el tope
        for (long bloque = 0; bloque * MINUTOS_POR_BLOQUE < total; bloque++) {

            long desde = Math.max(minutosGratis, bloque * MINUTOS_POR_BLOQUE);
            long hasta = Math.min(total, (bloque + 1) * MINUTOS_POR_BLOQUE);

            if (hasta > desde) {
                BigDecimal tramo = precioPorMinuto
                        .multiply(BigDecimal.valueOf(hasta - desde));
                coste = coste.add(tramo.min(topeDiario));
            }
        }

        return coste.setScale(2, RoundingMode.HALF_UP);
    }
}
