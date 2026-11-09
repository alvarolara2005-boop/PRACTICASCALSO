# Práctica 3 — Diseño y ejecución de pruebas de software de caja blanca y de caja negra

---

# Reparto y responsabilidades

| Integrante | Caja blanca: métodos de dominio | Caja negra: interfaz |
|---|---|---|
| **Álvaro Lara Lara**| `DojoSearchService.containsAllRequiredWeapons` y `DojoSearchService.findFirstAvailableDojo`  | Búsqueda de dojos (`/dojos`) |
| **Pablo García Pérez** | `ReservationDiscountService.findYoungestFighter` y `ReservationDiscountService.calculateBestDiscount` | Costes y bonificaciones (`/coste`) |

---

# Actividad 1 - Pruebas de caja blanca

## Álvaro Lara Lara

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

| Nodo | Contenido |
| -- | -- |
| 1 | entrada |
| 2 | D1: available == null |
| 3 | throw NPE "Available weapons are required" |
| 4 | D2: required == null |
| 5 | throw NPE "Required weapons are required" |
| 6 | D3: available.containsAll(required) |
| 7 | return true (D3 verdadero) |
| 8 | return false (D3 falso) |
| 9 | fin |

```mermaid
flowchart TB;
    1(("1")) --> 2(("2"));
    2 -->|"True"| 3(("3"));
    2 -->|"False"| 4(("4"));
    3 --> 9(("9"));
    4 -->|"True"| 5(("5"));
    4 -->|"False"| 6(("6"));
    5 --> 9;
    6 -->|"True"| 7(("7"));
    6 -->|"False"| 8(("8"));
    7 --> 9;
    8 --> 9;
```

#### 1.3 Complejidad ciclomática

| Fórmula | Cálculo | Resultado |
| --- | --- | --- |
| Predicados + 1 | 3 + 1 | 4 |
| Aristas - Nodos + 2 | 11 - 9 + 2 | 4 |
| Regiones | exterior + 3 internas | 4 |

V(G) = 4. Las tres fórmulas coinciden.

#### 1.4 Base de caminos independientes

| Camino | Recorrido | ¿Factible? | Justificación |
| --- | --- | --- | --- |
| C1 | 1-2-3-9 | Sí | available == null |
| C2 | 1-2-4-5-9 | Sí | required == null con available válido |
| C3 | 1-2-4-6-7-9 | Sí | todas las requeridas presentes (o required vacío) → true |
| C4 | 1-2-4-6-8-9 | Sí | falta alguna requerida → false |

Los 4 caminos son factibles y cubren las 11 aristas. V(G) = 4 = tamaño de la base.

#### 1.5 Tests con JUnit 5

En `es.um.calso.dojoreservation.domain.service.DojoSearchServiceWeaponsTest.java`

---

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

| Nodo | Contenido |
| -- | -- |
| 1 | entrada
| 2 | D1: dojos == null
| 3 | throw NPE "Dojos are required"
| 4 | D2: requiredWeapons == null
| 5 | throw NPE "Required weapons are required"
| 6 | D3: reservations == null
| 7 | throw NPE "Reservations are required"
| 8 | D4: trainingDate == null
| 9 | throw NPE "Training date is required"
| 10 | D5: bucle externo — ¿hay dojo?
| 11 | D6: containsAllRequiredWeapons(...)
| 12 | D7: bucle interno — ¿hay reserva?
| 13 | D8: r.dojoId().equals(dojo.id()) && !r.trainingDate().| equals(trainingDate)
| 14 | D10: !occupied
| 15 | return Optional.of(dojo)
| 16 | return Optional.empty()
| 17 | fin

```mermaid
flowchart TB;
    1(("1")) --> 2(("2"));
    2 -->|"True"| 3(("3"));
    2 -->|"False"| 4(("4"));
    3 --> 17(("17"));
    4 -->|"True"| 5(("5"));
    4 -->|"False"| 6(("6"));
    5 --> 17;
    6 -->|"True"| 7(("7"));
    6 -->|"False"| 8(("8"));
    7 --> 17;
    8 -->|"True"| 9(("9"));
    8 -->|"False"| 10(("10"));
    9 --> 17;
    10 -->|"True,<br>hay dojo"| 11(("11"));
    10 -->|"False,<br>no hay más<br>dojos"| 16(("16"));
    11 -->|"True,<br>compatible"| 12(("12"));
    11 -->|"False,<br>incompatible<br>→ siguiente dojo"| 10;
    12 -->|"True,<br>hay reserva"| 13(("13"));
    12 -->|"False,<br>no hay más<br>reservas"| 14(("14"));
    13 -->|"True,<br>ocupado → break<br>sale del bucle<br>interno"| 14;
    13 -->|"False,<br>no ocupado<br>→ siguiente reserva"| 12;
    14 -->|"True,<br>no ocupado"| 15(("15"));
    14 -->|"False,<br>ocupado<br>→ siguiente dojo"| 10;
    15 --> 17;
    16 --> 17;
```

#### 2.3 Complejidad ciclomática

N = 17 nodos · A = 27 aristas · Predicados = D1, D2, D3, D4, D5, D6, D7, D8, D10 = 9.

| Fórmula | Cálculo | Resultado
| --- | --- | --- |
| Predicados + 1 | 9 + 1 | 10 |
| Aristas - Nodos + 2 | 27 - 17 +2| 12 |
| Regiones | exterior + 9 internas | 10 |

Discrepancia: A − N + 2 da 12 porque el break introduce la arista 13→14, que sale del bucle interno sin pasar por su condición de terminación (12→14). Esa arista rompe la estructura propiamente anidada (properly nested) que asume la fórmula A − N + 2; por eso esa fórmula no es aplicable cuando hay break/continue/return internos.

Valor adoptado: V(G) = 10, que coincide por la definición de McCabe (nodos con ≥2 aristas de salida + 1) y por el recuento de regiones, y es el número de caminos linealmente independientes

#### 2.4 Base de caminos independientes

| Camino | Recorrido | ¿Factible? | Justificación |
| --- | --- | --- | --- |
| C1 | 1-2-3-17 | Sí | dojos == null |
| C2 | 1-2-4-5-17 | Sí | dojos válido, requiredWeapons == null |
| C3 | 1-2-4-6-7-17 | Sí | reservations == null |
| C4 | 1-2-4-6-8-9-17 | Sí | trainingDate == null |
| C5 | 1-2-4-6-8-10-16-17 | Sí | lista de dojos vacía |
| C6 | 1-2-4-6-8-10-11-10-16-17 | Sí | dojo incompatible → siguiente → agotado |
| C7 | 1-2-4-6-8-10-11-12-14-15-17 | Sí | compatible, sin reservas → devuelve |
| C8 | 1-2-4-6-8-10-11-12-13-12-14-15-17 | Sí | reserva no ocupante (A=false o B=false) → siguiente reserva → fin → devuelve |
| C9 | 1-2-4-6-8-10-11-12-13-14-10-16-17 | Sí | reserva ocupante (A=T,B=T) → break → siguiente dojo → agotado |
| C10 | 1-2-4-6-8-10-11-12-13-14-10-11-12-14-15-17 | Sí | primer dojo ocupado, segundo compatible y libre → devuelve segundo |

Los 10 caminos son factibles y cubren las 27 aristas. V(G) = 10 = tamaño de la base. 

#### 2.5 Trazabilidad camino → test

| Camino | Test JUnit |
| --- | --- |
| C1 | testGivenNullDojosWhenFindFirstThenNullPointerWithMessage |
| C2 | testGivenNullRequiredWeaponsWhenFindFirstThenNullPointerWithMessage |
| C3 | testGivenNullReservationsWhenFindFirstThenNullPointerWithMessage |
| C4 | testGivenNullTrainingDateWhenFindFirstThenNullPointerWithMessage |
| C5 | testGivenEmptyDojosWhenFindFirstThenEmpty |
| C6 | testGivenIncompatibleDojoWhenFindFirstThenEmpty |
| C7 | testGivenCompatibleDojoAndNoReservationsWhenFindFirstThenDojoReturned |
| C8 | testGivenReservationForOtherDojoWhenFindFirstThenDojoReturned, testGivenReservationForSameDojoSameDateWhenFindFirstThenDojoReturned, testGivenReservationForSameDojoOtherDateWhenFindFirstThenDojoReturned |
| C9 | testGivenFirstDojoOccupiedAndSecondIncompatibleWhenFindFirstThenEmpty |
| C10 | testGivenFirstDojoOccupiedAndSecondCompatibleAndFreeWhenFindFirstThenSecondReturned | 

#### 2.6 Cobertura de condiciones seleccionadas

Decisión asignada: reservation.dojoId().equals(dojo.id()) && !reservation.trainingDate().equals(trainingDate) (D8).

Condiciones atómicas:

A = reservation.dojoId().equals(dojo.id())

B = !reservation.trainingDate().equals(trainingDate)

| Condición | Caso que evalúa | Caso que evalúa falso | 
| --- | --- | --- |
| A | testGivenReservationForSameDojoOtherDateWhenFindFirstThenDojoReturned (A=T, B=T) | testGivenReservationForOtherDojoWhenFindFirstThenDojoReturned (A=F; B no se evalúa por cortocircuito de &&) |
| B | testGivenReservationForSameDojoOtherDateWhenFindFirstThenDojoReturned (B=T) | testGivenReservationForSameDojoSameDateWhenFindFirstThenDojoReturned (B=F) |

#### 2.7 Tests con JUnit 5

en `es.um.calso.dojoreservation.domain.service.DojoSearchServiceFindFirstTest.java`

---

### 3. Tabla resumen de trazabilidad

| Método | V(G) | Caminos | Tests implementados | Defecto detectado |
| --- | --- | --- | --- | --- |
| containsAllRequiredWeapons | 5 | C1–C4 | 5 | Ninguno (usa containsAll correctamente) |
| findFirstAvailableDojo | 11 | C1–C10 | 12 | D-1: condición de ocupación usa !equals en la fecha |

---

## Pablo García Pérez

### 1. Método `ReservationDiscountService.findYoungestFighter`

#### 1.1 Código analizado

```java
public Optional<Fighter> findYoungestFighter(
        List<Fighter> fighters,
        LocalDate referenceDate) {
    if (fighters == null) {
        throw new NullPointerException("Fighters are required");
    }
    if (referenceDate == null) {
        throw new NullPointerException("Reference date is required");
    }
    Fighter youngest = null;
    for (Fighter fighter : fighters) {
        if (fighter == null) {
            throw new NullPointerException("Null fighter");
        }
        if (youngest == null || fighter.birthDate().isBefore(youngest.birthDate())) {
            youngest = fighter;
        }
    }
    return Optional.ofNullable(youngest);
}
```

#### 1.2 Grafo de flujo

| Nodo | Contenido |
| -- | -- |
| 1 |  |
| 2 |  |

```mermaid
flowchart TB;
    1(("1")) --> 2(("2"));
```

#### 1.3 Complejidad ciclomática

#### 1.4 Base de caminos independientes

#### 1.5 Tests con JUnit 5

---

### 2. Método `ReservationDiscountService.calculateBestDiscount`

#### 2.1 Código analizado

```java
public Discount calculateBestDiscount(
        List<Fighter> fighters,
        LocalDate trainingDate) {
    if (fighters == null) {
        throw new NullPointerException("Fighters are required");
    }
    if (trainingDate == null) {
        throw new NullPointerException("Training date is required");
    }
    int bestPercentage = 0;
    Optional<Fighter> youngest = findYoungestFighter(fighters, trainingDate);
    if (youngest.isPresent()) {
        int age = youngest.orElseThrow().ageAt(trainingDate);
        if (age < 18) {
            bestPercentage = 15;
        }
    }
    for (Fighter fighter : fighters) {
        if (fighter == null) {
            throw new NullPointerException("Null fighter");
        }
        Optional<UnemploymentPeriod> period = fighter.unemployment();
        if (period.isPresent()) {
            long completeYears = period.orElseThrow().completeYearsAt(trainingDate);
            int unemploymentPercentage = 0;
            if (completeYears >= 3) {
                unemploymentPercentage = 30;
            } else if (completeYears > 2) {
                unemploymentPercentage = 20;
            } else if (completeYears >= 1) {
                unemploymentPercentage = 10;
            }
            if (unemploymentPercentage > bestPercentage) {
                bestPercentage = unemploymentPercentage;
            }
        }
    }
    return new Discount(bestPercentage);
}
```

#### 2.2 Grafo de flujo

#### 2.3 Complejidad ciclomática

#### 2.4 Base de caminos independientes

#### 2.5 Trazabilidad camino → test

#### 2.6 Cobertura de condiciones seleccionadas

#### 2.7 Tests con JUnit 5

---

### 3. Tabla resumen de trazabilidad

| Método | V(G) | Caminos | Tests implementados | Defecto detectado |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |

---

# Actividad 2 - Caja negra con Selenium

## 1. Identificación de clases de equivalencia

### 1.1 Factores de entrada

| Factor | Descripción | Dominio |
| --- | --- | --- |
| F1 | teamId | Identificadores conocidos {EQ-001..EQ-005} o cualquier otra cadena |
| F2 | trainingDate | Fecha ISO AAAA-MM-DD; relación con el reloj fijo (2026-10-20) |
| F3 | Estado de disponibilidad del dojo | Reserva existente con mismo dojoId y misma fecha |
| F4 | Compatibilidad de armas | Dojo contiene todas las armas requeridas del equipo |

### 1.2 Tabla de clases de equivalencia

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

### 1.3 Análisis de valores límite

| Factor | Valor inmediatamente inferior | Valor del límite | Valor inmediatamente superior | Casos elegidos y expectativas |
| --- | --- | --- | --- | --- |
| F2 fecha vs. reloj (2026-10-20) | 2026-10-19 (CE6) | 2026-10-20 (CE5, "hoy") | 2026-10-21 (CE4, "mañana") | 2026-10-19 → error fecha; 2026-10-20 → error fecha; 2026-10-21 → tabla normal |
| F1 teamId longitud | "" (vacío, CE3) | — | "EQ-001" (CE1) | ""/" " → error id requerido; EQ-001 → tabla |
| F1 teamId existencia | — | EQ-001..EQ-005 (CE1) | EQ-999 (CE2) | EQ-001 → tabla; EQ-999 → error inexistente |
| F3 ocupación | reserva D-001 @ 2027-11-20 (CE13, distinta fecha) | reserva D-001 @ 2026-11-20 (CE12, misma fecha) | — | Con EQ-001 y 2026-11-20 → D-001 ocupado; con EQ-001 y 2026-11-21 → D-001 libre |
| F4 compatibilidad | EQ-002 (Bo) contra D-001 (Katana+Bokken, CE11) | EQ-001 (Katana+Bokken) contra D-001 (CE10) |  |  |

### 1.5 Implementación Selenium

En `es.um.calso.dojoreservation.infrastructure.e2e.DojoSearchE2ETest.java`

### 1.6 Cobertura de clases de equivalencia

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

### 1.7 Trazabilidad clase → test

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

### 1.8 Ejecución y resultados

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

### 1.9 Defecto detectado (CN3)

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

---

# Actividad 3 - Calidad, defectos y regresión

## 1. Defectos y correcciones 

D-1 · findFirstAvailableDojo marca ocupado un dojo con reserva en otra fecha
Síntoma: al buscar EQ-001 con 2026-11-21, la fila de D-001 aparece "Ocupado" y se selecciona D-002, cuando la reserva de D-001 es de 2026-11-20 y 2027-11-20 (fechas distintas).

Prueba que lo detecta:

Unitaria: testGivenReservationForSameDojoOtherDateWhenFindFirstThenDojoReturned (RED antes de corregir).

E2E: testGivenReservationForDifferentDateWhenSearchThenNorthDojoIsFree (CN3, RED antes de corregir).

Causa: en DojoSearchService.findFirstAvailableDojo la condición de ocupación es
reservation.dojoId().equals(dojo.id()) && !reservation.trainingDate().equals(trainingDate).
La negación ! hace que cualquier reserva del mismo dojo en otra fecha marque el dojo como ocupado. La especificación (§1) exige comparar identificador y fecha: solo bloquea cuando coinciden ambos.

Corrección aplicada:

```java
if (reservation.dojoId().equals(dojo.id())
        && reservation.trainingDate().equals(trainingDate)) {
    occupied = true;
    break;
}
```
Regresión posterior:

mvn clean verify: 12/12 tests de DojoSearchServiceFindFirstTest GREEN (antes 11/12).

mvn -Pe2e clean verify: CN3 GREEN; CN1, CN2, CN4–CN7, CN9, CN10 siguen GREEN.

mvn -Pmutation test pitest:mutationCoverage: el mutante !equals→equals en la fecha pasa de superviviente a detectado.

Impacto: ninguno sobre otras clases; DojoEvaluationService ya comparaba con equals (correcto), por lo que la tabla de presentación y el servicio de dominio quedan ahora coherentes.

D-2 · Revisión de containsAllRequiredWeapons
