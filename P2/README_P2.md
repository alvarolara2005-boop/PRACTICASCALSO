# Práctica 2. Pruebas unitarias y análisis de calidad

**Calidad del Software · Curso 2026-2027**

## Descripción del caso práctico

Una empresa está desarrollando el software de un robot que ejecuta distintos programas de limpieza. Cada programa requiere una cantidad fija de energía y puede necesitar agua, jabón, cera o limpiacristales. El robot consulta las existencias antes de empezar: si falta algún producto, informa de las unidades necesarias y no consume nada; si hay suficiente de todos, realiza los descuentos. En esta práctica la energía identifica el consumo del programa, pero no se gestiona una batería.

Recibiréis un proyecto Java que compila y contiene defectos de comportamiento. Vuestro trabajo consiste en **descubrirlos mediante pruebas JUnit 5 diseñadas a partir de la especificación**, corregirlos y analizar los resultados de calidad obtenidos al terminar. No se facilita la cantidad ni la ubicación de los defectos.

Trabajaréis principalmente en **Eclipse**. El proyecto utiliza Maven y una organización basada en dominio, aplicación y adaptadores. La especificación que determina el comportamiento correcto está en `docs/specification.md`. **El código entregado no es la especificación**: precisamente puede contener errores.

### Bounded context y alcance del slice

El siguiente diagrama presenta los conceptos principales de **Robot Cleaning**, sus relaciones y su distribución entre las capas **Adapters**, **Application** y **Domain**. Permite distinguir el código comprendido en el slice de esta práctica de la funcionalidad que queda fuera de su desarrollo.

```mermaid
flowchart TB
    subgraph AD["ADAPTERS"]
        direction LR
        SERVICE["CleaningRobotService<br/>Adaptador de entrada"]
        STUB["StubCleaningInventory<br/>Suplantador para pruebas"]
        REAL["Inventario real<br/>Infraestructura no implementada"]
    end

    subgraph AP["APPLICATION"]
        direction LR
        PORT["IExecuteCleaningProgram<br/>Puerto de entrada"]
        UC["ExecuteCleaningProgramUseCase<br/>Caso de uso"]
        COMP["CleaningProgramByEnergyComparator<br/>Comparador"]
    end

    subgraph DOM["DOMAIN"]
        direction TB
        subgraph CORE["Modelo de dominio"]
            direction LR
            PROGRAM["CleaningProgram<br/>Entidad: id, name, energy, requirements"]
            RESULT["ExecutionResult<br/>executed, missingProducts"]
            INVENTORY["ICleaningInventory<br/>Puerto de salida del dominio"]
        end
        subgraph VALUES["Conceptos de valor"]
            direction LR
            ENERGY["Energy<br/>Objeto de valor"]
            PRODUCT["CleaningProduct<br/>WATER · SOAP · WAX · GLASS_CLEANER"]
        end
    end

    SERVICE --> PORT
    PORT --> UC
    UC --> PROGRAM
    UC --> RESULT
    UC --> INVENTORY
    COMP --> PROGRAM
    PROGRAM --> ENERGY
    PROGRAM --> PRODUCT
    RESULT --> PRODUCT
    STUB -.-> INVENTORY
    REAL -.-> INVENTORY

    classDef included fill:#E8F5EC,stroke:#248447,stroke-width:2px,color:#183B29
    classDef excluded fill:#FDEBEC,stroke:#CE3548,stroke-width:2px,color:#7D1F2C
    class SERVICE,STUB,PORT,UC,COMP,PROGRAM,RESULT,INVENTORY,ENERGY,PRODUCT included
    class REAL excluded

    style AD fill:#F7FAFD,stroke:#B8CADB,stroke-width:1px
    style AP fill:#F7FAFD,stroke:#B8CADB,stroke-width:1px
    style DOM fill:#F7FAFD,stroke:#B8CADB,stroke-width:1px
    style CORE fill:#FFFFFF,stroke:#D9E3EB
    style VALUES fill:#FFFFFF,stroke:#D9E3EB
Cómo interpretarlo: los elementos verdes pertenecen al slice y los rojos están fuera de su alcance o no se implementan en esta práctica. La interfaz ICleaningInventory está situada en la capa de dominio, aunque sea utilizada por el caso de uso de aplicación. El inventario real no se implementa: en las pruebas del caso de uso se desarrolla StubCleaningInventory dentro de src/test/java, para controlar las existencias sin depender de infraestructura real.

El diagrama es una visión arquitectónica, no sustituye a la especificación de comportamiento, que es la referencia para diseñar los casos de prueba.

Estructura del repositorio de GitHub
Cada grupo dispone de un único repositorio para todas las prácticas. Dentro de él se encuentra el proyecto Maven de la segunda práctica:

text
Repositorio-del-grupo/
├── .github/
│   └── workflows/
│       └── p2-quality.yml
├── P1/
└── P2/
    ├── README_P2.md
    ├── docs/
    ├── evidencias/
    └── P2_ALLPGP/
        ├── pom.xml
        └── src/
Para conectar las acciones de GitHub con la herramienta externa configuraremos un flujo de trabajo. El workflow se guardará en .github/workflows/ de la raíz del repositorio, no dentro de la carpeta P2/ ni del proyecto Java. La ruta exacta al proyecto Maven es P2/P2_ALLPGP/.

(El resto de las instrucciones originales se mantienen sin cambios).

Resolución del grupo
Integrantes, responsabilidades y enlaces de integración
Grupo: [Nombre del grupo, ej. ALLPGP]

[Álvaro Lara Lara] — Dominio y comparador

Clases de producción corregidas: Energy, CleaningProduct, CleaningProgram, CleaningProgramByEnergyComparator.

Clases de prueba añadidas: EnergyTest, CleaningProductTest, CleaningProgramTest, CleaningProgramByEnergyComparatorTest.

Rama: feature/dominio-comparador

Pull Request: [Enlace a la PR]

[Pablo García Pérez] — Suplantador y caso de uso

Clases de prueba añadidas: StubCleaningInventory, StubCleaningInventoryTest, ExecuteCleaningProgramUseCaseTest, CleaningRobotServiceTest.

Clases de producción corregidas: ExecuteCleaningProgramUseCase.

Rama: feature/stub-caso-uso

Pull Request: [Enlace a la PR]

Ambas ramas se integraron en main tras revisión cruzada. Los commits de cada integrante se realizaron exclusivamente en su propia rama, y el merge a main se hizo a través de las Pull Requests indicadas. El workflow de GitHub Actions sobre main finalizó en verde, publicando el análisis en SonarQube.

Casos de prueba
Se han diseñado casos a partir de docs/specification.md, cubriendo valores normales, límites, entradas inválidas, igualdad y ordenación, además de la ausencia de efectos cuando una ejecución se rechaza.

ID	Clase	Condición y resultado esperado	Método JUnit
E1	Energy	of(10) almacena 10	testGivenEnergy10WhenCreatingEnergyThenStoresValue10
E2	Energy	of(1) almacena 1 (límite mínimo válido)	testGivenEnergy1WhenCreatingEnergyThenStoresValue1
E3	Energy	of(0) lanza IllegalArgumentException	testGivenEnergy0WhenCreatingEnergyThenThrowsIllegalArgumentException
E4	Energy	of(-1) lanza IllegalArgumentException	testGivenEnergyMinus1WhenCreatingEnergyThenThrowsIllegalArgumentException
E5	Energy	Dos instancias con 10 son iguales	testGivenTwoEnergyValues10WhenComparingThenTheyAreEqual
E6	Energy	Dos instancias con 10 tienen mismo hashCode	testGivenTwoEnergyValues10WhenComparingThenHashCodesAreEqual
E7	Energy	Instancias con 10 y 20 no son iguales	testGivenEnergy10AndEnergy20WhenComparingThenTheyAreNotEqual
P1	CleaningProduct	WATER.toString() = "agua"	testGivenWaterWhenCallingToStringThenReturnsAgua
P2	CleaningProduct	SOAP.toString() = "jabón"	testGivenSoapWhenCallingToStringThenReturnsJabon
P3	CleaningProduct	WAX.toString() = "cera"	testGivenWaxWhenCallingToStringThenReturnsCera
P4	CleaningProduct	GLASS_CLEANER.toString() = "limpiacristales"	testGivenGlassCleanerWhenCallingToStringThenReturnsLimpiacristales
C1	CleaningProgram	Datos válidos se almacenan en getters	testGivenValidDataWhenCreatingProgramThenPropertiesAreStored
C2	CleaningProgram	id nulo lanza IllegalArgumentException	testGivenNullIdWhenCreatingProgramThenThrowsIllegalArgumentException
C3	CleaningProgram	id en blanco lanza IllegalArgumentException	testGivenBlankIdWhenCreatingProgramThenThrowsIllegalArgumentException
C4	CleaningProgram	name nulo lanza IllegalArgumentException	testGivenNullNameWhenCreatingProgramThenThrowsIllegalArgumentException
C5	CleaningProgram	name en blanco lanza IllegalArgumentException	testGivenBlankNameWhenCreatingProgramThenThrowsIllegalArgumentException
C6	CleaningProgram	energy nula lanza NullPointerException	testGivenNullEnergyWhenCreatingProgramThenThrowsNullPointerException
C7	CleaningProgram	requirements nulo lanza NullPointerException	testGivenNullRequirementsWhenCreatingProgramThenThrowsNullPointerException
C8	CleaningProgram	Cantidad negativa lanza IllegalArgumentException	testGivenNegativeQuantityWhenCreatingProgramThenThrowsIllegalArgumentException
C9	CleaningProgram	Mapa vacío permitido	testGivenEmptyRequirementsWhenCreatingProgramThenProgramIsCreated
C10	CleaningProgram	Igualdad por id aunque cambien nombre y requirements	testGivenTwoProgramsWithSameIdAndDifferentNameWhenComparingThenTheyAreEqual
C11	CleaningProgram	Igualdad por id con mismos id y requirements distintos	testGivenTwoProgramsWithSameIdAndDifferentRequirementsWhenComparingThenTheyAreEqual
C12	CleaningProgram	Distinto id ⇒ no iguales	testGivenTwoProgramsWithDifferentIdWhenComparingThenTheyAreNotEqual
C13	CleaningProgram	Copia defensiva del mapa original	testGivenProgramWhenModifyingOriginalMapThenProgramRequirementsAreUnchanged
C14	CleaningProgram	Mapa devuelto no modificable	testGivenProgramWhenGettingRequirementsThenMapIsUnmodifiable
K1	Comparator	Energía 10 < 20 ⇒ negativo	testGivenEnergy10AndEnergy20WhenComparingThenReturnsNegative
K2	Comparator	Energía 20 > 10 ⇒ positivo	testGivenEnergy20AndEnergy10WhenComparingThenReturnsPositive
K3	Comparator	Misma energía e id ⇒ 0	testGivenSameEnergyAndSameIdWhenComparingThenReturnsZero
K4	Comparator	Misma energía, ids A y B ⇒ orden por id	testGivenSameEnergyAndIdAAndIdBWhenComparingThenOrdersByIdAscending
K5	Comparator	Orden ascendente por energía al ordenar lista	testGivenProgramsWithDifferentEnergyWhenSortingThenOrdersByEnergyAscending
K6	Comparator	Desempate por id al ordenar lista	testGivenProgramsWithSameEnergyWhenSortingThenOrdersByIdAscending
S1	Stub	available(WATER) con stock 2 ⇒ 2	testGivenStockOfWater2WhenAskingAvailableThenReturns2
S2	Stub	Producto no registrado ⇒ 0	testGivenNoStockRegisteredWhenAskingAvailableThenReturns0
S3	Stub	Consumir 1 de 2 ⇒ queda 1	testGivenStockOfWater2WhenConsuming1ThenAvailableBecomes1
S4	Stub	Consumir 2 de 2 ⇒ queda 0	testGivenStockOfWater2WhenConsuming2ThenAvailableBecomes0
S5	Stub	Consumir 3 de 2 ⇒ excepción y stock intacto	testGivenStockOfWater2WhenConsuming3ThenThrowsIllegalArgumentExceptionAndStockUnchanged
S6	Stub	Consumir −1 ⇒ excepción y stock intacto	testGivenStockOfWater2WhenConsumingMinus1ThenThrowsIllegalArgumentExceptionAndStockUnchanged
S7	Stub	Dos instancias son independientes	testGivenTwoStubsWithDifferentStockWhenConsumingInOneThenOtherIsUnchanged
U1	UseCase	Agua=2, req=2 ⇒ ejecuta y queda 0	testGivenWater2WhenExecutingProgramRequiringWater2ThenExecutesAndWaterStockBecomes0
U2	UseCase	Agua=3, req=2 ⇒ ejecuta y queda 1	testGivenWater3WhenExecutingProgramRequiringWater2ThenExecutesAndWaterStockBecomes1
U3	UseCase	Agua=2, Jabón=0, req=2/1 ⇒ falla, falta jabón=1, sin consumo	testGivenWater2AndSoap0WhenExecutingProgramRequiringWater2AndSoap1ThenReportsSoap1MissingAndPreservesStock
U4	UseCase	Agua=0, Jabón=0, req=2/1 ⇒ faltan agua=2 y jabón=1	testGivenWater0AndSoap0WhenExecutingProgramRequiringWater2AndSoap1ThenReportsWater2AndSoap1Missing
U5	UseCase	Agua=1, req=2 ⇒ falta agua=1 (cálculo required−available)	testGivenWater1WhenExecutingProgramRequiringWater2ThenReportsWater1Missing
U6	UseCase	Programa sin productos ⇒ ejecuta sin consumir	testGivenEmptyRequirementsWhenExecutingThenExecutesWithoutConsuming
U7	UseCase	Requisito 0 ⇒ no consume	testGivenRequirementZeroWhenExecutingThenDoesNotConsume
U8	UseCase	Programa nulo ⇒ NullPointerException	testGivenNullProgramWhenExecutingThenThrowsNullPointerException
U9	UseCase	Ejecución rechazada ⇒ no se consume ningún producto	testGivenRejectedExecutionWhenCheckingThenNoProductIsConsumed
U10	UseCase	Inventario nulo en constructor ⇒ NullPointerException	testGivenNullInventoryWhenCreatingUseCaseThenThrowsNullPointerException
R1	Service	Agua=2, req=2 ⇒ executed=true	testGivenWater2WhenExecutingProgramRequiringWater2ThenExecutesAndReturnsExecutedTrue
R2	Service	Agua=0, req=1 ⇒ executed=false, falta agua=1	testGivenWater0WhenExecutingProgramRequiringWater1ThenReturnsExecutedFalseWithWater1Missing
R3	Service	Inventario nulo ⇒ NullPointerException	testGivenNullInventoryWhenCreatingServiceThenThrowsNullPointerException
Defectos detectados y corregidos
Cada defecto se descubrió primero mediante un test que falló. Los commits de corrección se hicieron después, manteniendo el test revelador en la batería final.

ID	Prueba que revela el defecto	Esperado / observado	Causa y corrección	Commit
D1	testGivenTwoEnergyValues10WhenComparingThenTheyAreEqual	Iguales / Distintas	Energy.equals comparaba por identidad (==). Se corrigió a comparación por units.	[Hash del commit]
D2	testGivenTwoEnergyValues10WhenComparingThenHashCodesAreEqual	Mismo hash / Distinto hash	Energy.hashCode usaba System.identityHashCode. Se corrigió a Integer.hashCode(units).	[Hash del commit]
D3	testGivenEnergy0WhenCreatingEnergyThenThrowsIllegalArgumentException	Excepción / Sin excepción	Energy.of permitía el 0. Se cambió la validación a units <= 0.	[Hash del commit]
D4	testGivenGlassCleanerWhenCallingToStringThenReturnsLimpiacristales	"limpiacristales" / "cristales"	Etiqueta incorrecta en el enum. Se cambió a "limpiacristales".	[Hash del commit]
D5	testGivenTwoProgramsWithDifferentIdWhenComparingThenTheyAreNotEqual	Distintos / Iguales	CleaningProgram.equals/hashCode usaban name en lugar de id. Corregido a id.	[Hash del commit]
D6	testGivenProgramWhenModifyingOriginalMapThenProgramRequirementsAreUnchanged	Sin cambios / Cambia	No había copia defensiva del mapa. Se añadió Map.copyOf en el constructor.	[Hash del commit]
D7	testGivenProgramWhenGettingRequirementsThenMapIsUnmodifiable	UnsupportedOperationException / Sin excepción	El mapa devuelto era el original mutable. Se resolvió con Map.copyOf.	[Hash del commit]
D8	testGivenEnergy10AndEnergy20WhenComparingThenReturnsNegative	Negativo / Positivo	El comparador usaba compare(second, first), invirtiendo el orden. Corregido a compare(first, second).	[Hash del commit]
D9	testGivenWater2WhenExecutingProgramRequiringWater2ThenExecutesAndWaterStockBecomes0	Ejecuta / Marca faltante	La condición era available <= required en lugar de available < required. Corregida.	[Hash del commit]
D10	testGivenWater1WhenExecutingProgramRequiringWater2ThenReportsWater1Missing	Falta 1 / Falta 2	El caso de uso reportaba required en lugar de required − available. Corregido.	[Hash del commit]
D11	testGivenWater2AndSoap0WhenExecutingProgramRequiringWater2AndSoap1ThenReportsSoap1MissingAndPreservesStock	Sin consumo / Consume agua	El caso de uso consumía en el mismo bucle en el que detectaba faltantes. Se reescribió en dos pasadas.	[Hash del commit]
Suplantador y desarrollo incremental
Se sustituye la dependencia ICleaningInventory, el puerto de salida del dominio que el caso de uso necesita para consultar y consumir productos. El inventario real pertenece a la infraestructura y no se implementa en esta práctica.

El uso de una interfaz como puerto de salida permite que ExecuteCleaningProgramUseCase dependa de la abstracción y no de una implementación concreta. De este modo, podemos inyectar un doble de prueba (StubCleaningInventory) ubicado en src/test/java/com/calso/robot/application/usecases/, sin necesidad de infraestructura real ni de base de datos.

El suplantador:

acepta un mapa de existencias iniciales;

devuelve 0 para productos no registrados;

descuenta cantidades válidas sin dejar valores negativos;

lanza IllegalArgumentException si se intenta consumir una cantidad negativa o superior a la disponible, sin modificar el estado;

conserva su propio estado por instancia.

Gracias a ello, ExecuteCleaningProgramUseCaseTest verifica tanto el ExecutionResult devuelto (ejecutado / faltantes) como las existencias finales del inventario, sin depender del módulo real. CleaningRobotServiceTest reutiliza el mismo stub para probar la delegación desde el adaptador de entrada.

Indicadores de calidad
Herramienta	Resultado obtenido	Evidencia
JUnit 5	44 tests, 0 fallos (ejecución completa del proyecto)	evidencias/02-junit-verde.png
JaCoCo (instrucciones y ramas)	Instrucciones: 65.2% (según SonarQube)	target/site/jacoco/index.html 
PIT (mutation coverage y test strength)	[Poner MC]% / [Poner TS]%	target/pit-reports/ 
SonarQube (Quality Gate e incidencias)	Quality Gate: Passed. 12 Code Smells.	