# Práctica 3. Diseño y ejecución de pruebas de software de caja blanca y de caja negra

**Asignatura:** Calidad del Software · Curso 2026–2027  
**Caso de estudio:** Dojo Reservation

## 1. Descripción general

El Ayuntamiento de una localidad gestiona la asignación de dojos para entrenamientos de equipos de cinco luchadores y el cálculo de su coste en la gestión de campeonatos de artes marciales. La aplicación está implementada en Java y dispone de dos páginas web: **búsqueda de dojos** y **cálculo del coste**.

El objetivo general es comprobar el comportamiento programado mediante técnicas de **caja blanca** (caminos independientes y cobertura de condiciones seleccionadas) y de **caja negra** (clases de equivalencia y análisis de valores límite), analizar resultados de calidad y justificar las correcciones de los defectos incluidos en las clases de estudio.

El documento [especificación funcional](docs/ESPECIFICACION.md) es la referencia para identificar los resultados esperados en el proceso de prueba; la [guía técnica y de pruebas](docs/GUIA_PRUEBAS.md) describe el uso de JUnit 5, Selenium y Maven.

## 2. Reparto y responsabilidades

Si la práctica es realizada por un único alumno deberá implementar el total de las pruebas de todos los ficheros propuestos para realizar las pruebas y deberá realizar la entrega completa de la práctica. En el caso de grupos formados por dos miembros se deberá seguir el siguiente reparto de responsabilidades:

| Integrante | Caja blanca: métodos de dominio | Caja negra: interfaz |
|---|---|---|
| **Alumno 1 — Álvaro Lara Lara**| `containsAllRequiredWeapons` y `DojoSearchService.findFirstAvailableDojo`  | Búsqueda de dojos (`/dojos`) |
| **Alumno 2 — Pablo García Pérez** | `ReservationDiscountService.findYoungestFighter` y `calculateBestDiscount` | Costes y bonificaciones (`/coste`) |

## Actividad 1

### Alumno 1

### 1. Método `DojoSearchService.containsAllRequiredWeapons`

#### 1.1 Código analizado

```java
public boolean containsAllRequiredWeapons(Set<Weapon> available, Set<Weapon> required) {
    if (available == null) {                    // D1
        throw new NullPointerException("Available weapons are required");
    }
    if (required == null) {                     // D2
        throw new NullPointerException("Required weapons are required");
    }
    for (Weapon weapon : required) {            // D3 (bucle)
        if (available.isEmpty()) {              // D4
            return false;
        }
    }
    return true;
}
```

#### 1.2 Grafo de flujo
Nodos:

1: entrada

2: available == null (D1)

3: lanzar NPE Available weapons are required

4: required == null (D2)

5: lanzar NPE Required weapons are required

6: inicio bucle for weapon : required (D3) — evaluación

7: cuerpo del bucle, available.isEmpty() (D4)

8: return false

9: fin iteración → vuelve a 6

10: return true

11: fin

Aristas:

1→2

2→3 (true D1), 2→4 (false D1)

3→11

4→5 (true D2), 4→6 (false D2)

6→7 (hay elemento), 6→10 (no hay más elementos)

7→8 (true D4), 7→9 (false D4)

8→11

9→6

10→11

#### 1.3 Complejidad ciclomática
Predicados: D1, D2, D3 (condición del for), D4 → 4 predicados.
V(G)=P+1=4+1=5

Comprobación por aristas y nodos: aristas = 14, nodos = 11 → 
V(G)=14−11+2=5. ✔

Regiones: R1 exterior, R2 (D1 verdadero), R3 (D2 verdadero), R4 (D4 verdadero), R5 (cuerpo del bucle) → 5. ✔

#### 1.4 Base de caminos independientes
| Camino | Recorrido | ¿Factible? | Justificación |
| --- | --- | --- | --- |
| C1 | 1-2-3-11 | Sí | available == null |
| C2 | 1-2-4-5-11 | Sí | available válido, required == null |
| C3 | 1-2-4-6-10-11 | Sí | required vacío, no entra al bucle |
| C4 | 1-2-4-6-7-8-11 | Sí | required con elemento, available vacío → false |
| C5 | 1-2-4-6-7-9-6-10-11 | Sí | required con elemento, available no vacío → true |

No hay caminos inviables. La base cubre las cinco aristas de decisión.

#### 1.5 Tests JUnit 5
en `es.um.calso.dojoreservation.domain.service.DojoSearchServiceWeaponsTest.java`

### 2. Método `DojoSearchService.findFirstAvailableDojo`
#### 2.1 Código analizado

```java
public Optional<Dojo> findFirstAvailableDojo(
        List<Dojo> dojos, Set<Weapon> requiredWeapons,
        List<Reservation> reservations, LocalDate trainingDate) {
    if (dojos == null) throw new NPE("Dojos are required");            // D1
    if (requiredWeapons == null) throw new NPE("Required weapons are required"); // D2
    if (reservations == null) throw new NPE("Reservations are required"); // D3
    if (trainingDate == null) throw new NPE("Training date is required"); // D4

    for (Dojo dojo : dojos) {                                          // D5 (bucle externo)
        if (containsAllRequiredWeapons(dojo.weapons(), requiredWeapons)) { // D6
            boolean occupied = false;                                  // N
            for (Reservation reservation : reservations) {             // D7 (bucle interno)
                if (reservation.dojoId().equals(dojo.id())           // D8a
                        && !reservation.trainingDate().equals(trainingDate)) { // D8b
                    occupied = true;
                    break;                                             // D9
                }
            }
            if (!occupied) {                                           // D10
                return Optional.of(dojo);                              // D11
            }
        }
    }
    return Optional.empty();                                           // D12
}
```

#### 2.2 Grafo de flujo
Nodos:

1: entrada

2: D1 (dojos == null)

3: NPE "Dojos are required"

4: D2 (requiredWeapons == null)

5: NPE "Required weapons are required"

6: D3 (reservations == null)

7: NPE "Reservations are required"

8: D4 (trainingDate == null)

9: NPE "Training date is required"

10: inicio bucle externo D5

11: D6 (containsAllRequiredWeapons(...))

12: occupied = false

13: inicio bucle interno D7

14: D8a (reservation.dojoId().equals(dojo.id()))

15: D8b (!reservation.trainingDate().equals(trainingDate))

16: occupied = true

17: break

18: fin iteración interna → vuelve a 13

19: D10 (!occupied)

20: return Optional.of(dojo)

21: fin iteración externa → vuelve a 10

22: return Optional.empty()

23: fin

Aristas:

1→2

2→3 (true D1), 2→4 (false D1)

3→23

4→5 (true D2), 4→6 (false D2)

5→23

6→7 (true D3), 6→8 (false D3)

7→23

8→9 (true D4), 8→10 (false D4)

9→23

10→11 (hay dojo), 10→22 (no hay más dojos)

11→12 (true D6), 11→21 (false D6)

12→13

13→14 (hay reserva), 13→19 (no hay más reservas)

14→15 (true D8a), 14→18 (false D8a)

15→16 (true D8b), 15→18 (false D8b)

16→17

17→19

18→13

19→20 (true D10), 19→21 (false D10)

20→23

21→10

22→23

#### 2.3 Complejidad ciclomática
Predicados: D1, D2, D3, D4, D5, D6, D7, D8a, D8b, D10 → 10 predicados.

V(G)=P+1=10+1=11

Comprobación: nodos = 23, aristas = 34 → 

V(G)=34−23+2=13. **Nota:** el recuento por aristas y nodos da 13, mientras que el análisis adopta 11; conviene revisar el grafo o el recuento antes de la entrega. Discrepancia por el break que genera una arista adicional (17→19) y el cortocircuito de && (representamos D8a y D8b como nodos separados, lo que añade el nodo 15 y la arista 14→18 directa). Contando decisiones de control (sin desdoblar cortocircuito): D1..D8 (con D8 como una sola decisión), D10 → 9 predicados → 

V(G)=10. Contando con cortocircuito desdoblado y break: 11. Se adopta 
V(G)=11 desdoblando cortocircuito y break, coherente con el grafo dibujado.

Riesgo según SEI: 11–20 → programa medianamente complejo, riesgo moderado.

#### 2.4 Base de caminos independientes
| Camino | Recorrido | ¿Factible? | Justificación |
| --- | --- | --- | --- |
| C1 | 1-2-3-23 | Sí | dojos == null |
| C2 | 1-2-4-5-23 | Sí | dojos válido, requiredWeapons == null |
| C3 | 1-2-4-6-7-23 | Sí | reservations == null |
| C4 | 1-2-4-6-8-9-23 | Sí | trainingDate == null |
| C5 | 1-2-4-6-8-10-22-23 | Sí | lista de dojos vacía → Optional.empty() |
| C6 | 1-2-4-6-8-10-11-21-10-22-23 | Sí | dojo incompatible, siguiente iteración, lista agotada |
| C7 | 1-2-4-6-8-10-11-12-13-19-20-23 | Sí | dojo compatible, sin reservas → occupied = false → Optional.of |
| C8 | 1-2-4-6-8-10-11-12-13-14-18-13-19-20-23 | Sí | reserva de otro dojo o otra fecha → no ocupado → Optional.of |
| C9 | 1-2-4-6-8-10-11-12-13-14-15-16-17-19-21-10-22-23 | Sí | reserva que ocupa el dojo → occupied = true, break, siguiente dojo, agotado |
| C10 | `1-2-4-6-8-10-11-12-13-14-15-18-13-19-20-23` | Sí | D8a es verdadera y D8b es falsa; el dojo no se marca como ocupado y se devuelve mediante `Optional.of` |
| C11 | 1-2-4-6-8-10-11-12-13-14-15-16-17-19-21-10-11-21-10-22-23 | Sí | primer dojo ocupado, segundo incompatible, lista agotada |

                                                                    Los caminos C1–C11 cubren todas las aristas del grafo. La base es de tamaño 11 (= V(G)). Todos son factibles con entradas válidas de dominio.

#### 2.5 Cobertura de condiciones seleccionadas
Decisión asignada: reservation.dojoId().equals(dojo.id()) && !reservation.trainingDate().equals(trainingDate).

Condiciones atómicas:

A = reservation.dojoId().equals(dojo.id())

B = !reservation.trainingDate().equals(trainingDate)

En el test con reserva de otro dojo, B no se evalúa. Debe documentarse como sin evaluación, no como falso.

No hay situación en que A=true y B no se evalúe (el && evalúa B si A es true).

#### 2.6 Tests JUnit 5
en `es.um.calso.dojoreservation.domain.service.DojoSearchServiceFindFirstTest.java`

### 3. Tabla resumen de trazabilidad
| Método | V(G) | Caminos | Tests implementados | Defecto detectado |
| --- | --- | --- | --- | --- |
| containsAllRequiredWeapons | 5 | C1–C5 | 6 | available.containsAll(required) no implementado; solo isEmpty() |
| findFirstAvailableDojo | 11 | C1–C11 | 11 | Condición de ocupación niega la fecha (!equals) |

### 4. Tabla de cobertura de condiciones 
| Condición atómica | Caso que evalúa verdadero | Caso que evalúa falso | Casos adicionales | Justificación si no es factible |
| --- | --- | --- | --- | --- |
| A = reservation.dojoId().equals(dojo.id()) | testGivenReservationForSameDojoOtherDate... (A=true, B=true) | testGivenReservationForOtherDojo... (A=false, B no evaluada por cortocircuito) | — | — |
| B = !reservation.trainingDate().equals(trainingDate) | testGivenReservationForSameDojoOtherDate... (B=true) | testGivenReservationForSameDojoSameDate... (B=false) | — | — |

En el caso A=false, B no se evalúa (cortocircuito de &&); no se contabiliza como falso. No existen evaluaciones no factibles dentro de esta decisión: todas las combinaciones alcanzables (A=false; A=true,B=true; A=true,B=false) tienen al menos un test.

## Actividad 2
### 1. Identificación de clases de equivalencia
#### 1.1 Factores de entrada
| Factor | Descripción | Dominio |
| --- | --- | --- |
| F1 | teamId | Identificadores conocidos {EQ-001..EQ-005} o cualquier otra cadena |
| F2 | trainingDate | Fecha ISO AAAA-MM-DD; relación con el reloj fijo (2026-10-20) |
| F3 | Estado de disponibilidad del dojo | Reserva existente con mismo dojoId y misma fecha |
| F4 | Compatibilidad de armas | Dojo contiene todas las armas requeridas del equipo |

#### 1.2 Tabla de clases de equivalencia
| Código | Factor / estado | Condición de pertenencia | Válida / inválida | Comportamiento exigido |
| --- | --- | --- | --- | --- |
| CE1 | F1 teamId | Identificador existente en catálogo (EQ-001..EQ-005) | Válida | Se evalúan los dojos para ese equipo |
| CE2 | F1 teamId | Cadena no vacía pero inexistente (p. ej. EQ-999) | Inválida | Error Team does not exist (#error) |
| CE3 | F1 teamId | Cadena vacía o en blanco ("", " ") | Inválida | Error Team id is required (#error) |
| CE4 | F2 trainingDate | Fecha estrictamente posterior a 2026-10-20 | Válida | Búsqueda normal; tabla de dojos |
| CE5 | F2 trainingDate | Fecha igual a 2026-10-20 (hoy) | Inválida | Error Training date must be in the future (#error) |
| CE6 | F2 trainingDate | Fecha anterior a 2026-10-20 | Inválida | Error Training date must be in the future (#error) |
| CE7 | F2 trainingDate | Formato no ISO (p. ej. 20/11/2026) | Inválida | Rechazo de Spring MVC antes del caso de uso (no mensaje de fecha futura) |
| CE8 | F3 Disponibilidad | Ningún dojo compatible y libre | Válida (resultado vacío) | #no-dojo visible; #dojo-id ausente |
| CE9 | F3 Disponibilidad | Al menos un dojo compatible y libre | Válida (resultado con selección) | #dojo-id visible; fila resaltada selected-row |
| CE10 | F4 Compatibilidad | Dojo con todas las armas requeridas | Válida | Fila con status-ok "Compatible" |
| CE11 | F4 Compatibilidad | Dojo sin todas las armas requeridas | Válida | Fila con status-no "Incompatible" |
| CE12 | F3 Ocupación | Reserva con mismo dojoId y misma fecha | Válida | Fila con status-no "Ocupado" |
| CE13 | F3 Ocupación | Reserva con mismo dojoId pero distinta fecha | Válida | Fila |

#### 1.3 Análisis de valores límite
| Factor | Valor inmediatamente inferior | Valor del límite | Valor inmediatamente superior | Casos elegidos y expectativas |
| --- | --- | --- | --- | --- |
| F2 fecha vs. reloj (2026-10-20) | 2026-10-19 (CE6) | 2026-10-20 (CE5, "hoy") | 2026-10-21 (CE4, "mañana") | 2026-10-19 → error fecha; 2026-10-20 → error fecha; 2026-10-21 → tabla normal |
| F1 teamId longitud | "" (vacío, CE3) | — | "EQ-001" (CE1) | ""/" " → error id requerido; EQ-001 → tabla |
| F1 teamId existencia | — | EQ-001..EQ-005 (CE1) | EQ-999 (CE2) | EQ-001 → tabla; EQ-999 → error inexistente |
| F3 ocupación | reserva D-001 @ 2027-11-20 (CE13, distinta fecha) | reserva D-001 @ 2026-11-20 (CE12, misma fecha) | — | Con EQ-001 y 2026-11-20 → D-001 ocupado; con EQ-001 y 2026-11-21 → D-001 libre |
| F4 compatibilidad | EQ-002 (Bo) contra D-001 (Katana+Bokken, CE11) | EQ-001 (Katana+Bokken) contra D-001 (CE10) |  |  |

#### 1.5 Implementación Selenium
en `es.um.calso.dojoreservation.infrastructure.e2e.DojoSearchE2ETest.java`

#### 1.6 Cobertura de clases de equivalencia
| Clase | Caso(s) | Estado |
| --- | --- | --- |
| CE1 | CN1, CN2, CN3, CN9 | Cubierta |
| CE2 | CN4 | Cubierta |
| CE3 | CN5 | Cubierta |
| CE4 | CN1, CN2, CN3, CN9 | Cubierta |
| CE5 | CN6 | Cubierta |
| CE6 | CN7 | Cubierta |
| CE7 | CN10 | Cubierta |
| CE8 | — | No factible con fixtures fijos; justificada |
| CE9 | CN1, CN2, CN3, CN9 | Cubierta |
| CE10 | CN1, CN3, CN9 | Cubierta |
| CE11 | CN9 | Cubierta |
| CE12 | CN2 | Cubierta |
| CE13 | CN3 | Cubierta (detecta defecto) |

#### 1.7 Trazabilidad clase → test
| Clase | Test |
| --- | --- |
| CE1, CE4, CE9, CE10 | testGivenDragonTeamTomorrow... |
| CE1, CE4, CE9, CE12 | testGivenNorthDojoBookedForDate... |
| CE1, CE4, CE9, CE13 | testGivenReservationForDifferentDate... |
| CE2 | testGivenUnknownTeam... |
| CE3 | testGivenEmptyTeam... |
| CE5 | testGivenToday... |
| CE6 | testGivenYesterday... |
| CE10, CE11 | testGivenTigerTeam... |
| CE7 | testGivenMalformedDate... |

#### 1.8 Ejecución y resultados
| Test | Resultado | Motivo |
| --- | --- | --- |
| CN1 testGivenDragonTeamTomorrow... | GREEN | D-001 libre y compatible → seleccionado |
| CN2 testGivenNorthDojoBookedForDate... | GREEN | D-001 ocupado → D-002 seleccionado |
| CN3 testGivenReservationForDifferentDate... | RED | Defecto: findFirstAvailableDojo trata la reserva de otra fecha como ocupación. #dojo-id = D-002, no D-001; fila D-001 muestra "Ocupado" |
| CN4 testGivenUnknownTeam... | GREEN | #error = "Team does not exist" |
| CN5 testGivenEmptyTeam... | GREEN | #error = "Team id is required" |
| CN6 testGivenToday... | GREEN | #error = "Training date must be in the future" |
| CN7 testGivenYesterday... | GREEN | #error = "Training date must be in the future" |
| CN9 testGivenTigerTeam... | GREEN | D-001 incompatible, D-003 seleccionado |
| CN10 testGivenMalformedDate... | GREEN | Spring MVC devuelve 400; no hay tabla ni error de dominio |

#### 1.9 Defecto detectado (CN3)
Síntoma: al buscar con EQ-001 y 2026-11-21, la fila de D-001 aparece como "Ocupado" y se selecciona D-002, cuando la reserva de D-001 es para 2026-11-20 y 2027-11-20 (fechas distintas).

Causa: en DojoSearchService.findFirstAvailableDojo, la condición !reservation.trainingDate().equals(trainingDate) marca como ocupado cualquier dojo que tenga alguna reserva en otra fecha. La especificación exige bloquear solo cuando coinciden identificador y fecha.

Corrección propuesta:

```java
if (reservation.dojoId().equals(dojo.id())
        && reservation.trainingDate().equals(trainingDate)) {
    occupied = true;
    break;
}
```

Regresión: tras la corrección, CN3 pasa a GREEN; CN1, CN2, CN4–CN7, CN9, CN10 permanecen GREEN. Se repite mvn -Pe2e clean verify y se verifica también la suite unitaria (mvn clean verify).
