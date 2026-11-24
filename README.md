# ISO2-2026-GrupoC201-Testing-P1

Problema 1 de la segunda parte del trabajo teórico de Ingeniería del Software II: **coste de la estancia en un aparcamiento**.

Enunciado: [docs/T2_ESP_2026-27v1.1.pdf](docs/T2_ESP_2026-27v1.1.pdf)

## Reparto por pasos

| Paso | Qué se hace | Pareja |
|---|---|---|
| 1 | Implementación del programa (este proyecto) | Pareja 1: Sheila Calzado y Celia barba |
| 2 | Diseño de casos de prueba (ejercicio teórico) | Pareja 2: Yasmine Hamzat y Miriam Gómez |
| 3 | Implementación de las pruebas en JUnit | Pareja 3: Mateo Bretaño, Juan Manuel Ferrero y Tomás Rodríguez |

## Requisitos

- Java 17
- Maven 3.9 o superior

## Compilar y probar

```
mvn clean verify
```

El informe de cobertura queda en `target/site/jacoco/index.html`. La cobertura mínima exigida es del 80 % de líneas.

## Ejecutar

```
mvn clean compile
java -cp target/classes es.uclm.esi.iso2.aparcamiento.interfaz.Main
```

## Estructura

- `src/main/java/.../dominio`: lógica de negocio (sin entrada ni salida de datos).
- `src/main/java/.../interfaz`: interfaz de línea de comandos.
- `src/test/java`: pruebas (las implementa la pareja 3 en el Paso 3).
- `docs`: enunciado.

## Decisiones de diseño


### Reglas de negocio

| Decisión | Elección | Justificación |
|---|---|---|
| Minutos gratuitos | Los primeros 30 minutos **de la estancia**, una sola vez | Es la lectura más directa de "los primeros 30 minutos son gratuitos". |
| Minutos de pago | 0,05 € por cada minuto a partir del minuto 30 | Lo dice el enunciado. |
| Tope máximo diario | 20 € por cada **bloque de 24 horas contado desde la entrada**, no por día natural | Con el día natural habría que partir la estancia a medianoche y una estancia de 23:00 a 01:00 tendría dos topes. El bloque de 24 h depende solo de la entrada. |
| Redondeo | **Minuto empezado**: 30 min 1 s se cuentan como 31 minutos | Es lo habitual en los tickets de aparcamiento y evita regalar tiempo. |
| Salida igual a la entrada | Válida: estancia de 0 minutos y coste 0,00 € | El enunciado solo exige error si la salida es **anterior**. |
| Estancias de varios días | Cada bloque de 24 h cobra como máximo 20 €. Los minutos gratuitos solo se descuentan en el primer bloque | En el primer bloque el tope se alcanza a los 430 minutos y en los siguientes a los 400. |

Ejemplos de coste con la tarifa por defecto:

| Minutos de estancia | Coste |
|---|---|
| 0, 29, 30 | 0,00 € |
| 31 | 0,05 € |
| 60 | 1,50 € |
| 430 | 20,00 € |
| 431 | 20,00 € |
| 1440 (24 h) | 20,00 € |
| 1441 | 20,05 € |
| 1470 | 21,50 € |
| 2880 (48 h) | 40,00 € |

### Datos y validación

- **Tipo de fecha:** `LocalDateTime`. No lleva zona horaria, así que no se tiene en cuenta el cambio de hora de verano/invierno (limitación conocida).
- **Validación en el constructor:** `Estancia` es inmutable y comprueba sus datos al crearse. Una `Estancia` que existe siempre es correcta.
- **Excepciones propias, no comprobadas:** `FechaNulaException` (alguna fecha nula) y `SalidaAnteriorAEntradaException` (salida anterior a la entrada), ambas hijas de `EstanciaInvalidaException`. `TarifaInvalidaException` para parámetros de tarifa incorrectos.
- **Dinero con `BigDecimal`:** siempre con 2 decimales y redondeo `HALF_UP`. Así no hay errores de coma flotante con 0,05.
- **Tarifa configurable:** `Tarifa.porDefecto()` usa los valores del enunciado (30 min, 0,05 €, 20 €), y el constructor admite otros. Exige minutos gratuitos ≥ 0, precio > 0 y tope > 0.
- **Método principal:** `Tarifa.calcularCoste(entrada, salida)`, además de `calcularCoste(Estancia)`.
- **Sin fecha actual:** el dominio no usa `LocalDateTime.now()`. Todas las fechas llegan por parámetro, así que el resultado es siempre el mismo para los mismos datos.

### Arquitectura

- **Dominio separado de la interfaz:** el paquete `dominio` no usa `System.in` ni `System.out` y no conoce el paquete `interfaz`.
- **Interfaces `ILectorDatos` e `IEscritorPantalla`:** el flujo (`AplicacionInterfaz`) depende de ellas y no de la consola. Las implementaciones reciben el `InputStream` y el `PrintStream`, así se puede probar con datos simulados.
- **Excepciones para el flujo de la interfaz:**
  - Una fecha con formato incorrecto o inexistente (31/04) se captura y se vuelve a pedir. El formato es `dd/MM/aaaa HH:mm`, con modo estricto.
  - Si el dominio rechaza la estancia (por ejemplo, salida anterior), se muestra el mensaje y se pide de nuevo.
  - Si se acaba la entrada de datos, `FinDeEntradaException` cierra el programa sin error.

### Limitaciones conocidas

- No se contemplan husos horarios ni el cambio de hora.
- El cálculo recorre los bloques de 24 h, por lo que para estancias de años enteros hace una iteración por día (aceptable para un aparcamiento).
- Los datos no se guardan: cada ejecución calcula estancias sueltas.
