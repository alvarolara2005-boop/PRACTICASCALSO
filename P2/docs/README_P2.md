# Práctica 2 — Pruebas unitarias y análisis de calidad

---

## Status de P2_ALLPGP en SonarQube:

- [![Quality Gate Status](https://sast.inf.um.es/api/project_badges/measure?project=PRACTICASCALSO_ALL_PGP&metric=alert_status&token=sqb_4e9e2d0861d8bf51e367061a3b9218edb52c1d35)](https://sast.inf.um.es/dashboard?id=PRACTICASCALSO_ALL_PGP)

---

## Integrantes, responsabilidades y enlaces de integración

- **Grupo:** ALLPGP

### Álvaro Lara Lara — Dominio y comparador

- **Clases de prueba añadidas:** `EnergyTest`, `CleaningProductTest`, `CleaningProgramTest`, `CleaningProgramByEnergyComparatorTest`.

- **Clases de producción corregidas:** `Energy`, `CleaningProduct`, `CleaningProgram`, `CleaningProgramByEnergyComparator`.

- Rama de trabajo: `feature/dominio-comparador`

- Pull Request: *Enlace pendiente*

### Pablo García Pérez — Suplantador y caso de uso

- **Clases de prueba añadidas/editadas:** `StubCleaningInventory`, `StubCleaningInventoryTest`, `ExecuteCleaningProgramUseCaseTest`, `CleaningRobotServiceTest`, `ExecutionResultTest`.

- **Clases de producción corregidas:** `ExecutionResult`, `ExecuteCleaningProgramUseCase`

- Rama de trabajo: `feature/stub-caso-uso`

- Pull Request: [Commit 75f9031](https://github.com/alvarolara2005-boop/PRACTICASCALSO/commit/75f9031b1819a70f60b89be523bf6086ce19817e)

Ambas ramas se integraron en main tras revisión cruzada. Los commits de cada integrante se realizaron exclusivamente en su propia rama, y el merge a main se hizo a través de las Pull Requests indicadas. El workflow de GitHub Actions sobre main finalizó en verde, publicando el análisis en SonarQube.

---

## Casos de prueba

Se han diseñado casos de prueba a partir de `docs/specification.md`, cubriendo valores normales, límite, entradas inválidas, igualdad y ordenación, además de la ausencia de efectos cuando una ejecución se rechaza.

Se muestran a continuación estos casos de prueba diseñados en forma de tabla:

| ID | Clase | Condición y resultado esperado | Método JUnit |
| --- | --- | --- | --- |
| E1 | EnergyTest | of(10) almacena 10 | testGivenEnergy10WhenCreatingEnergyThenStoresValue10 |
| E2 | EnergyTest | of(1) almacena 1 (límite mínimo válido) | testGivenEnergy1WhenCreatingEnergyThenStoresValue1 |
| E3 | EnergyTest | of(0) lanza IllegalArgumentException | testGivenEnergy0WhenCreatingEnergyThenThrowsIllegalArgumentException |
| E4 | EnergyTest | of(-1) lanza IllegalArgumentException | testGivenEnergyMinus1WhenCreatingEnergyThenThrowsIllegalArgumentException |
| E5 | EnergyTest | Dos instancias con 10 son iguales | testGivenTwoEnergyValues10WhenComparingThenTheyAreEqual |
| E6 | EnergyTest | Dos instancias con 10 tienen mismo hashCode | testGivenTwoEnergyValues10WhenComparingThenHashCodesAreEqual |
| E7 | EnergyTest | Instancias con 10 y 20 no son iguales | testGivenEnergy10AndEnergy20WhenComparingThenTheyAreNotEqual |
| P1 | CleaningProductTest | WATER.toString() = "agua" | testGivenWaterWhenCallingToStringThenReturnsAgua |
| P2 | CleaningProductTest | SOAP.toString() = "jabón" | testGivenSoapWhenCallingToStringThenReturnsJabon |
| P3 | CleaningProductTest | WAX.toString() = "cera" | testGivenWaxWhenCallingToStringThenReturnsCera |
| P4 | CleaningProductTest | GLASS_CLEANER.toString() = "limpiacristales" | testGivenGlassCleanerWhenCallingToStringThenReturnsLimpiacristales |
| C1 | CleaningProgramTest | Datos válidos se almacenan en getters | testGivenValidDataWhenCreatingProgramThenPropertiesAreStored |
| C2 | CleaningProgramTest | id nulo lanza IllegalArgumentException | testGivenNullIdWhenCreatingProgramThenThrowsIllegalArgumentException |
| C3 | CleaningProgramTest | id en blanco lanza IllegalArgumentException | testGivenBlankIdWhenCreatingProgramThenThrowsIllegalArgumentException |
| C4 | CleaningProgramTest | name nulo lanza IllegalArgumentException | testGivenNullNameWhenCreatingProgramThenThrowsIllegalArgumentException |
| C5 | CleaningProgramTest | name en blanco lanza IllegalArgumentException | testGivenBlankNameWhenCreatingProgramThenThrowsIllegalArgumentException |
| C6 | CleaningProgramTest | energy nula lanza NullPointerException | testGivenNullEnergyWhenCreatingProgramThenThrowsNullPointerException |
| C7 | CleaningProgramTest | requirements nulo lanza NullPointerException | testGivenNullRequirementsWhenCreatingProgramThenThrowsNullPointerException |
| C8 | CleaningProgramTest | Cantidad negativa lanza IllegalArgumentException | testGivenNegativeQuantityWhenCreatingProgramThenThrowsIllegalArgumentException |
| C9 | CleaningProgramTest | Mapa vacío permitido | testGivenEmptyRequirementsWhenCreatingProgramThenProgramIsCreated |
| C10 | CleaningProgramTest | Igualdad por id aunque cambien nombre y requirements | testGivenTwoProgramsWithSameIdAndDifferentNameWhenComparingThenTheyAreEqual |
| C11 | CleaningProgramTest | Igualdad por id con mismos id y requirements distintos | testGivenTwoProgramsWithSameIdAndDifferentRequirementsWhenComparingThenTheyAreEqual |
| C12 | CleaningProgramTest | Distinto id ⇒ no iguales | testGivenTwoProgramsWithDifferentIdWhenComparingThenTheyAreNotEqual |
| C13 | CleaningProgramTest | Copia defensiva del mapa original | testGivenProgramWhenModifyingOriginalMapThenProgramRequirementsAreUnchanged |
| C14 | CleaningProgramTest | Mapa devuelto no modificable | testGivenProgramWhenGettingRequirementsThenMapIsUnmodifiable |
| K1 | CleaningProgramByEnergyComparatorTest | Energía 10 < 20 ⇒ negativo | testGivenEnergy10AndEnergy20WhenComparingThenReturnsNegative |
| K2 | CleaningProgramByEnergyComparatorTest | Energía 20 > 10 ⇒ positivo | testGivenEnergy20AndEnergy10WhenComparingThenReturnsPositive |
| K3 | CleaningProgramByEnergyComparatorTest | Misma energía e id ⇒ 0 | testGivenSameEnergyAndSameIdWhenComparingThenReturnsZero |
| K4 | CleaningProgramByEnergyComparatorTest | Misma energía, ids A y B ⇒ orden por id | testGivenSameEnergyAndIdAAndIdBWhenComparingThenOrdersByIdAscending |
| K5 | CleaningProgramByEnergyComparatorTest | Orden ascendente por energía al ordenar lista | testGivenProgramsWithDifferentEnergyWhenSortingThenOrdersByEnergyAscending |
| K6 | CleaningProgramByEnergyComparatorTest | Desempate por id al ordenar lista | testGivenProgramsWithSameEnergyWhenSortingThenOrdersByIdAscending |
| S1 | StubCleaningInventoryTest | Constructor con mapa nulo lanza NullPointerException | testGivenNullMapWhenCreatingNewStubCleaningInventoryThenThrowsNullPointerException |
| S2 | StubCleaningInventoryTest | Constructor con existencia -1 lanza IllegalArgumentException | testGivenValueMinus1WhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException |
| S3 | StubCleaningInventoryTest | Constructor con clave nula en el mapa lanza IllegalArgumentException | testGivenKeyNullWhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException |
| S4 | StubCleaningInventoryTest | Constructor con valor nulo en el mapa lanza IllegalArgumentException | testGivenValueNullWhenCreatingNewStubCleaningInventoryThenThrowsIllegalArgumentException |
| S5 | StubCleaningInventoryTest | Constructor con jabón=0 no lanza excepción (límite mínimo válido) | testGivenSoap0WhenCreatingNewStubCleaningInventoryThenDoesNotThrowAnyException |
| S6 | StubCleaningInventoryTest | Constructor con jabón=1 no lanza excepción | testGivenSoap1WhenCreatingNewStubCleaningInventoryThenDoesNotThrowAnyException |
| S7 | StubCleaningInventoryTest | available(null) lanza NullPointerException | testGivenNullCleaningProductWhenCheckingAvailableProductThenThrowsNullPointerException |
| S8 | StubCleaningInventoryTest | Producto no registrado (jabón): available devuelve 0 | testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenCheckingSoapQuantityThenResturns0 |
| S9 | StubCleaningInventoryTest | Limpiacristales=1 registrado: available devuelve 1 | testGivenInventoryWithGlassCleaner1WhenCheckingGlassCleanerQuantityThenReturns1 |
| S10 | StubCleaningInventoryTest | consume(null, 1) lanza NullPointerException | testGivenNullCleaningProductWhenConsumingProductThenThrowsNullPointerException |
| S11 | StubCleaningInventoryTest | consume con cantidad -1 lanza IllegalArgumentException | testGivenQuantityLessThan0WhenConsumingCleaningProductThenThrowsIllegalArgumentException |
| S12 | StubCleaningInventoryTest | Limpiacristales=1 y consume 2 (supera existencias) lanza IllegalArgumentException | testGivenGlassCleanerQuantity1WhenConsumingGlassCleanerQuantity2ThenThrowsIllegalArgumentException |
| S13 | StubCleaningInventoryTest | consume de 0 unidades de producto no registrado (jabón) no lanza excepción | testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenConsumingQuantity0ThenDoesNotThrowAnyException |
| S14 | StubCleaningInventoryTest | consume de 0 unidades de limpiacristales no altera la existencia (sigue en 1) | testGivenInventoryWithNoSoapAddedAtInventoryCreationWhenConsumingQuantity0ThenQuantityStays0 |
| S15 | StubCleaningInventoryTest | Limpiacristales=1 y consume 1: la existencia pasa a 0 | testGivenGlassCleanerQuantity1WhenConsumingQuantity1ThenQuantity0 |
| R1 | ExecutionResultTest | Constructor con missingProducts nulo lanza NullPointerException	| testGivenNullMissingProductsWhenCreatingExecutionResultThenThrowsNullPointerException |
| R2 | ExecutionResultTest | executed=true y mapa vacío: executed() devuelve true	| testGivenExecutedTrueAndEmptyMissingProductsWhenCreatingResultThenExecutedIsTrue |
| R3 | ExecutionResultTest | executed=true y mapa vacío: missingProducts() está vacío	| testGivenExecutedTrueAndEmptyMissingProductsWhenCreatingResultThenMissingProductsIsEmpty |
| R4 | ExecutionResultTest | executed=true y missingProducts con agua=5 y cera=3 lanza IllegalArgumentException	| testGivenExecutedTrueAndMissingProductsWater5Wax3WhenCreatingResultThenThrowsIllegalArgumentException |
| R5 | ExecutionResultTest | executed=false y mapa vacío: executed() devuelve false	| testGivenExecutedFalseAndEmptyMissingProductsWhenCreatingResultThenExecutedIsFalse |
| R6 | ExecutionResultTest | executed=false y mapa vacío: missingProducts() está vacío	| testGivenExecutedFalseAndEmptyMissingProductsWhenCreatingResultThenMissingProductsIsEmpty |
| R7 | ExecutionResultTest | executed=false con faltantes agua=2 y jabón=1: executed() devuelve false | testGivenExecutedFalseAndWater2AndSoap1MissingWhenCreatingResultThenExecutedIsFalse |
| R8 | ExecutionResultTest | executed=false con faltantes agua=2 y jabón=1: missingProducts() conserva agua=2 y jabón=1	| testGivenExecutedFalseAndWater2AndSoap1MissingWhenCreatingResultThenMissingProductsDoesNotChange |
| R9 | ExecutionResultTest | Mapa devuelto por missingProducts() no modificable: añadir limpiacristales=1 lanza UnsupportedOperationException | testGivenResultWithMissingProductsWater2AndSoap1WhenPuttingGlassCleaner1InReturnedMapThenThrowsUnsupportedOperationException |
| U1 | ExecuteCleaningProgramUseCaseTest | Constructor con inventario nulo lanza NullPointerException | testGivenNullInventoryWhenCreatingCleaningProgramUseCaseThenThrowsNullPointerException |
| U2 | ExecuteCleaningProgramUseCaseTest | execute(null) lanza NullPointerException | testGivenNullCleaningProgramWhenExecutingCleaningProgramThenThrowsNullPointerException |
| U3 | ExecuteCleaningProgramUseCaseTest | Agua=5 y cera=3, programa requiere agua=5 y cera=5: executed() devuelve false | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenExecutedIsFalse |
| U4 | ExecuteCleaningProgramUseCaseTest | Agua=5 y cera=3, programa requiere agua=5 y cera=5: el inventario se conserva (agua=5, cera=3) | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenPreservesStock |
| U5 | ExecuteCleaningProgramUseCaseTest | Agua=5 y cera=3, programa requiere agua=5 y cera=5: missingProducts es {cera=2} | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenResultMissingProductsWax2 |
| U6 | ExecuteCleaningProgramUseCaseTest | Existencias exactas (agua=5, cera=3) y programa requiere agua=5 y cera=3: executed() devuelve true | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenExecutedIsTrue |
| U7 | ExecuteCleaningProgramUseCaseTest | Existencias exactas (agua=5, cera=3) y programa requiere agua=5 y cera=3: el inventario queda en agua=0 y cera=0 | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenWater0Wax0 |
| U8 | ExecuteCleaningProgramUseCaseTest | Existencias exactas (agua=5, cera=3) y programa requiere agua=5 y cera=3: missingProducts está vacío | testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenMissingProductsIsEmpty |
| U9 | ExecuteCleaningProgramUseCaseTest | Existencias superiores (agua=6, cera=4) y programa requiere agua=5 y cera=3: executed() devuelve true | testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenExecutedIsTrue |
| U10 | ExecuteCleaningProgramUseCaseTest | Existencias superiores (agua=6, cera=4) y programa requiere agua=5 y cera=3: el inventario queda en agua=1 y cera=1 | testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenWater1Wax1 |
| U11 | ExecuteCleaningProgramUseCaseTest | Existencias superiores (agua=6, cera=4) y programa requiere agua=5 y cera=3: missingProducts está vacío | testGivenWater6AndWax4WhenExecutingProgramRequiringWater5AndWax3ThenMissingProductsIsEmpty |
| U12 | ExecuteCleaningProgramUseCaseTest | Programa sin productos con agua=5 y cera=3: executed() devuelve true | testGivenWater5AndWax3WhenExecutingProgramWithoutProductRequirementsThenExecutedIsTrue |
| U13 | ExecuteCleaningProgramUseCaseTest | Programa sin productos con agua=5 y cera=3: el inventario no cambia (agua=5, cera=3) | testGivenWater5AndWax3WhenExecutingProgramWithoutProductRequirementsThenStockIsPreserved |
| U14 | ExecuteCleaningProgramUseCaseTest | Programa con requisitos agua=0 y cera=0 con agua=5 y cera=3: executed() devuelve true | testGivenWater5AndWax3WhenExecutingProgramRequiringWater0AndWax0ThenExecutedIsTrue |
| U15 | ExecuteCleaningProgramUseCaseTest | Programa con requisitos agua=0 y cera=0 con agua=5 y cera=3: el inventario no cambia (agua=5, cera=3) | testGivenWater5AndWax3WhenExecutingProgramRequiringWater0AndWax0ThenStockIsPreserved |
| V1 | CleaningRobotServiceTest | Constructor con inventario nulo lanza NullPointerException | testGivenCleaningInventoryNullWhenCreatingCleaningRobotServiceThenThrowsNullPointerException
| V2 | CleaningRobotServiceTest | executeProgram(null) lanza NullPointerException | testGivenNullCleaningProgramWhenExecutingProgramThenThrowsNullPointerException
| V3 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=1: executed() devuelve true	| testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenResultExecutedIsTrue
| V4 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=1: missingProducts está vacío | testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenResultMissingProductsIsEmpty
| V5 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=2: executed() devuelve false | testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenResultExecutedIsFalse
| V6 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=2: missingProducts es {limpiacristales=1} | testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenResultMissingProductGlassCleaner1
| V7 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=2: la existencia sigue en 1	| testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner2ThenGlassCleanerStockRemains1
| V8 | CleaningRobotServiceTest | Limpiacristales=1 y programa requiere limpiacristales=1: la existencia pasa a 0 | testGivenGlassCleaner1WhenExecutingProgramRequiringGlassCleaner1ThenGlassCleanerStockBecomes0

---

## Defectos detectados y corregidos

Cada defecto se descubrió primero mediante un test que falló. Los commits de corrección se hicieron tras descubrir cada defecto, manteniendo el test revelador en la batería final.

| ID | Prueba que revela el defecto | Esperado / observado | Causa y corrección | Commit |
| --- | --- | --- | --- | --- |
| D1 | testGivenTwoEnergyValues10WhenComparingThenTheyAreEqual | Iguales / Distintas | Energy.equals comparaba por identidad (==). Se corrigió a comparación por units. | [Hash del commit] |
| D2 | testGivenTwoEnergyValues10WhenComparingThenHashCodesAreEqual | Mismo hash / Distinto hash | Energy.hashCode usaba System.identityHashCode. Se corrigió a Integer.hashCode(units). | [Hash del commit] |
| D3 | testGivenEnergy0WhenCreatingEnergyThenThrowsIllegalArgumentException | Excepción / Sin excepción | Energy.of permitía el 0. Se cambió la validación a units <= 0. | [Hash del commit] |
| D4 | testGivenGlassCleanerWhenCallingToStringThenReturnsLimpiacristales | "limpiacristales" / "cristales" | Etiqueta incorrecta en el enum. Se cambió a "limpiacristales". | [Hash del commit] |
| D5 | testGivenTwoProgramsWithDifferentIdWhenComparingThenTheyAreNotEqual | Distintos / Iguales | CleaningProgram.equals/hashCode usaban name en lugar de id. Corregido a id. | [Hash del commit] |
| D6 | testGivenProgramWhenModifyingOriginalMapThenProgramRequirementsAreUnchanged | Sin cambios / Cambia | No había copia defensiva del mapa. Se añadió Map.copyOf en el constructor. | [Hash del commit] |
| D7 | testGivenProgramWhenGettingRequirementsThenMapIsUnmodifiable | UnsupportedOperationException / Sin excepción | El mapa devuelto era el original mutable. Se resolvió con Map.copyOf. | [Hash del commit] |
| D8 | testGivenEnergy10AndEnergy20WhenComparingThenReturnsNegative | Negativo / Positivo | El comparador usaba compare(second, first), invirtiendo el orden. Corregido a compare(first, second). | [Hash del commit] |
| D9 | testGivenWater2WhenExecutingProgramRequiringWater2ThenExecutesAndWaterStockBecomes0 | Ejecuta / Marca faltante | La condición era available <= required en lugar de available < required. Corregida. | [Hash del commit] |
| D10 | testGivenWater1WhenExecutingProgramRequiringWater2ThenReportsWater1Missing | Falta 1 / Falta 2 | El caso de uso reportaba required en lugar de required − available. Corregido. | [Hash del commit] |
| D11 | testGivenWater2AndSoap0WhenExecutingProgramRequiringWater2AndSoap1ThenReportsSoap1MissingAndPreservesStock | Sin consumo / Consume agua | El caso de uso consumía en el mismo bucle en el que detectaba faltantes. Se reescribió en dos pasadas. | [Hash del commit] |
| D12 | [testGivenExecutedTrueAndMissingProductsWater5Wax3WhenCreatingResultThenMissingProductsIsEmpty](./evidencias/capturas/ExecutionResultTest1.png) | true / false | No se puede devolver un mapa de productos faltantes con contenido si el resultado de ejecución es true. Se añade funcionalidad para lanzar IllegalArgumentException en caso de intentar crear un ExecutionResult de estas características. | 92d1236bb09a402b12143d47d4ce6c0f6a52e5e9 |
| D13 | [testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenMissingProductsIsEmpty](./evidencias/capturas/ExecuteCleaningProgramUseCaseTest1.png) | true / false | El mapa de productos faltantes devuelto debería de estar vacío, ya que las cantidades de inventario y requeridas son iguales. Se ajusta el código para que sólo se consuman productos del inventario en caso de que no exista ningún producto faltante. | 14acf5061e078751fd0f2e06d3a425805835578e |
| D14 | [testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax5ThenResultMissingProductsWax2](./evidencias/capturas/ExecuteCleaningProgramUseCaseTest2.png) | {cera=2} / {cera=5, agua=5} | No se restan correctamente las cantidades de cada producto del inventario. Se ajusta la forma de insertar elementos en el mapa de productos faltantes para que el valor de cada producto sea el de requerido menos disponible. | 14acf5061e078751fd0f2e06d3a425805835578e |
| D15 | [testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenExecutedIsTrue](./evidencias/capturas/ExecuteCleaningProgramUseCaseTest3.png) | true / false | No se ejecuta correctamente el programa de limpieza aunque las cantidades de cada producto de inventario y requeridas sean las de un programa válido. Se ajusta el condicional para que el caso de que se tenga que añadir un producto al mapa de productos faltantes sea de disponible < requerido, y no disponible <= requerido | 14acf5061e078751fd0f2e06d3a425805835578e |
| D16 | [testGivenWater5AndWax3WhenExecutingProgramRequiringWater5AndWax3ThenWater0Wax0](./evidencias/capturas/ExecuteCleaningProgramUseCaseTest4.png) | 0 / 5 | No se restan correctamente las cantidades de cada producto del inventario. Se ajusta la forma de insertar elementos en el mapa de productos faltantes para que el valor de cada producto sea el de requerido menos disponible. | 14acf5061e078751fd0f2e06d3a425805835578e |

---

## Suplantador y desarrollo incremental

**Dependencia sustituida:** `ExecuteCleaningProgramUseCase` necesita consultar y descontar existencias a través de la interfaz `ICleaningInventory`, el puerto de salida definido en la capa de dominio. El inventario real pertenece a la infraestructura y no se implementa en esta práctica, por lo que en su lugar se usa como suplantador `StubCleaningInventory`, lo que permite que ``ExecuteCleaningProgramUseCase`` dependa de la abstracción y no de una implementación concreta con estructura real.

**Por qué la interfaz lo permite:** El caso de uso depende de la abstracción (la interfaz `ICleaningInventory`) y no de una clase concreta, y recibe el inventario por constructor. Basta con inyectar otra implementación que cumpla el mismo contrato que propone la interfaz, sin cambiar una línea de producción ni necesitar base de datos o infraestructura real.

**Qué hace el suplantador `StubCleaningInventory`:**
- Acepta unas existencias iniciales y valida que ninguna sea nula ni negativa.
- Devuelve 0 para los productos no registrados (los completa a 0 en el constructor).
- El método `consume` descuenta cantidades válidas y lanza la excepción `IllegalArgumentException` si la cantidad es negativa o supera las existencias, sin modificar el estado del inventario proporcionado.
- Cada instancia mantiene su propio `EnumMap`, por lo que las pruebas no comparten estado.

**Utilidad de este suplantador:** Gracias al stub se ha podido probar y corregir el caso de uso y el servicio de entrada `CleaningRobotService` antes de que se desarrollara el inventario real. Las pruebas verifican tanto el `ExecutionResult` devuelto como las existencias finales (por ejemplo, que no cambian cuando se rechaza la ejecución por productos faltantes). Cuando se implemente el inventario real, bastará con que cumpla con el diseño de la interfaz `ICleaningInventory`, además, se podrá reutilizar los mismos tests para probar su diseño.

---

## Indicadores de calidad

| Herramienta | Resultado obtenido | Evidencia |
| --- | --- | --- |
| JUnit 5 | 78 tests ejecutados, 0 fallos, 0 errores | ![Captura Final JUnit](./evidencias/capturas/Captura%20Final%20JUnit.png) |
| JaCoCo (instrucciones y ramas) | Instrucciones: 100% - Ramas cubiertas: 90% | ![Captura Final JaCoCo](./evidencias/capturas/Captura%20Final%20JaCoCo.png) - [index.html](./evidencias/jacoco/index.html) |
| PIT (mutation coverage y test strength) | Line coverage: 100% - Mutation coverage: 92% - Test strength: 92% | ![Captura Final PIT](./evidencias/capturas/Captura%20Final%20PIT.png) - [index.html](./evidencias/pit-reports/index.html) |
| SonarQube (Quality Gate e incidencias) | Quality Gate: Passed - 8 code smells - Coverage: 96.3% | ![Captura Final SonarQube](./evidencias/capturas/Captura%20Final%20SonarQube.png) |

**Commit de `main` analizado:** [Commit 75f9031](https://github.com/alvarolara2005-boop/PRACTICASCALSO/commit/75f9031b1819a70f60b89be523bf6086ce19817e) / [Action de GitHub](https://github.com/alvarolara2005-boop/PRACTICASCALSO/actions/runs/42516579834)

**Enlace al vídeo externo (oculto/no listado):** [https://youtu.be/k0XIechRA9o](https://youtu.be/k0XIechRA9o)