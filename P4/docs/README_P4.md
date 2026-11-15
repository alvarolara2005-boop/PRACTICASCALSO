# P4 · Entrega de ATDD, TDD y pruebas orientadas a objetos

> **Plantilla de alumnado.** Sustituir los textos `[...]`, eliminar instrucciones auxiliares y conservar enlaces verificables. No copiar ejemplos de commits como si fueran propios. Este README se ubicará en `P4/README_P4.md` dentro del repositorio del grupo. Los enlaces a vídeos serán externos al repositorio.

## 1. Grupo, repositorio y estado

| Campo | Información |
|---|---|
| Integrantes | [Nombre y apellidos, cuenta GitHub de cada participante] |
| Repositorio | [URL] |
| Rama de integración / commit final | [main o rama / hash / URL permanente] |
| Ruta de proyecto Maven | `P4/` |
| Fecha y versión de entrega | [Fecha, hash] |
| Resultados de ejecución final | [Tests JUnit: N; escenarios Cucumber: N; fallos: N; omitidos: N] |
| Workflow GitHub Actions | [URL de ejecución final] |
| SonarQube / Quality Gate CalSo | [URL, estado, fecha] |

**Resumen de lo entregado (máximo 100 palabras):** [qué historias y escenarios se han realizado, qué limitaciones quedan y qué decisiones son relevantes].

## 2. Plan de trabajo y dependencias

| Incremento / HU / escenario | Responsable | Revisor | Dependencia y condición de inicio | Rama / PR | Estado |
|---|---|---|---|---|---|
| **Etapa 1: alumno 1, HU-01 y HU-02 hasta BDD-05d** | [Alumno 1] | [Alumno 2] | Proyecto base compila | [PR de relevo] | [GREEN + UML HU-02] |
| **Etapa 2: alumno 2, HU-03 BDD-06–09, 10–12 y 13d** | [Alumno 2] | [Alumno 1] | **Commit GREEN integrado de etapa 1** | [PR final] | [GREEN + UML HU-03] |
| **HU-03: límites 15/15, 12/18, 18/12, 11/19 y 19/11 en JUnit** | [Alumno 2] | [Alumno 1] | Dominio combinado | [tests/commits] | [resultados JUnit] |

**Commit obligatorio de relevo de la etapa 1:** [HASH y URL]. **Ejecución de regresión en ese commit:** [URL/reporte]. **UML de HU-02:** [URL]. La etapa 2 comienza después; no se exige alternancia continua ni implementación simultánea. Para grupos de una persona, la misma persona realiza ambas etapas.

**Incidencias de coordinación:** [bloqueo real, alternativa aplicada, revisión cruzada y resultado. Una frase por incidencia relevante].

## 3. Política de commits y técnica elegida de auditoría

**Técnica elegida:** [recomendada: historial de hitos por escenario + tabla de hashes; o describir alternativa igualmente auditable].

**Política adoptada antes del trabajo:**

  1. **Granularidad:** [qué constituye un commit comprobable y cuándo se separan test, implementación, refactorización y regresión].
  2. **Mensajes:** usar obligatoriamente `TIPO HU-XX BDD-YY: descripción en español` e indicar hash real y resultados. Tipos: `ATDD-TEST`, `ATDD-FEAT`, `TDD-TEST`, `TDD-FEAT`, `TDD-REFACTOR`. Para un cambio de varios escenarios, usar `BDD-YY+BDD-ZZ`. [Enlazar ejemplo propio].
  3. **RED auténtico:** [cómo se registra el test escrito antes de producción; diferencia fallo de aserción, compilación por API ausente y error del entorno].
  4. **GREEN mínimo:** [cómo se observa la ejecución y regresión después de la implementación mínima].
  5. **REFACTOR:** [cómo se comprueba que no cambia el comportamiento; no se fuerzan refactorizaciones ni commits inexistentes].
  6. **ATDD exterior:** [cómo se enlazan escenario Gherkin inicial, Cucumber pending/undefined o fallido, Then comprobados, aceptación final y CI].
  7. **Conservación y auditoría:** [si no se usa squash/rebase/amend sobre commits auditados; URL a PR; `git show HASH_REAL` y `git diff HASH_A HASH_B`; dónde están logs o reportes].
  8. **Excepciones a la política:** [incidencias y justificación; marcar «ninguna» cuando corresponda].

**Ejemplo real explicado:** [enlace a hash y por qué es ATDD, RED, GREEN o REFACTOR; no usar un hash ficticio].

**Ejemplos de la nomenclatura:**

```text
ATDD-TEST HU-02 BDD-05b: especificar suma incorrecta de ejercicios
TDD-TEST HU-02 BDD-05b: exigir coherencia de duraciones
TDD-FEAT HU-02 BDD-05b: validar suma de ejercicios
TDD-REFACTOR HU-02 BDD-05a+BDD-05b: ubicar la regla en el dominio
ATDD-FEAT HU-02 BDD-05b: verificar rechazo y ausencia de registro
```

Los commits se realizan **tras ejecutar la prueba y antes de cambiar de fase**. Un `undefined/pending` es evidencia ATDD inicial, no RED de lógica; una prueba ya verde no se fuerza a fallar. En REFACTOR se pueden actualizar juntos código y pruebas cuando evolucione un contrato interno, preservando los resultados pactados.

## 4. Reglas de negocio y especificación ATDD

| Regla | HU | Escenario BDD y feature | Datos representativos/límites | Resultado observable acordado | Estado |
|---|---|---|---|---|---|
| [RG-...] | [...] | [ruta + línea o enlace] | [...] | [...] | GREEN/PENDING/otro |

**Acuerdos y dudas:** [cambios de contrato confirmados y fecha de acuerdo; no inventar requisitos para hacer pasar pruebas]. Justificar brevemente los `Scenario Outline` y los escenarios individuales elegidos.

## 5. Auditoría del escenario intermedio de cada participante

**Alumno 1:** seleccionar un escenario significativo de HU-02. **Alumno 2:** seleccionar uno de HU-03, preferentemente BDD-13d, que exige introducir una nueva regla de dominio; ninguno presenta únicamente el laboratorio guiado ni una variante que ya pase sin desarrollo. Cada persona debe aportar commits y vídeo verificables de su etapa. Se evaluará con vídeo individual y commits auténticos.

### Participante A — [identificación]

- **HU, escenario y regla:** [...]
- **Por qué es intermedio y significativo:** [regla nueva, colaboración o cambio de diseño].
- **Vídeo individual:** [URL externa, máximo 6–7 min].
- **Rama, PR y revisión:** [...]

| Hito | Commit/URL permanente | Test o archivo | Ejecución y resultado observable | Minuto del vídeo |
|---|---|---|---|---|
| ATDD-TEST: feature + Cucumber inicial | [...] | [...] | [pending / undefined / RED real; indicar cuál] | [...] |
| TDD-TEST: primer JUnit RED | [...] | [...] | [fallo de aserción / API todavía inexistente; no confundir con infraestructura] | [...] |
| TDD-FEAT: GREEN mínimo | [...] | [...] | [test y regresión; resultado] | [...] |
| TDD-TEST / TDD-FEAT: segundo microciclo, si existe | [...] | [...] | [...] | [...] |
| TDD-REFACTOR (si hubo) o decisión de no realizarla | [...] | [...] | [comportamiento conservado] | [...] |
| Pruebas de clase, contrato, colaboración | [...] | [...] | [regla protegida] | [...] |
| ATDD-FEAT: Cucumber final y regresión | [...] | [...] | [todos los Then reales + nº de pruebas] | [...] |
| CI final | [...] | [...] | [URL de Actions] | [...] |

**Qué hizo emerger el diseño:** [relacionar el test con la clase/interfaz/regla de dominio. Explicar un doble si aparece y sus límites].

### Participante B — [identificación; eliminar sección en entregas individuales]

- **HU, escenario y regla:** [...]
- **Por qué es intermedio y significativo:** [...]
- **Vídeo individual:** [URL externa].
- **Rama, PR y revisión:** [...]

| Hito | Commit/URL permanente | Test o archivo | Ejecución y resultado observable | Minuto del vídeo |
|---|---|---|---|---|
| Contrato/feature + Cucumber inicial | [...] | [...] | [...] | [...] |
| Primer JUnit RED | [...] | [...] | [...] | [...] |
| GREEN mínimo | [...] | [...] | [...] | [...] |
| TDD-REFACTOR (si hubo) / pruebas OO | [...] | [...] | [...] | [...] |
| ATDD-FEAT: Cucumber final + regresión | [...] | [...] | [...] | [...] |
| CI final | [...] | [...] | [...] | [...] |

**Qué hizo emerger el diseño:** [...]

## 6. UML: evolución real y decisiones OO

**Fuente de los diagramas:** [archivos Mermaid `classDiagram`, PlantUML `.puml` o equivalente; incluir exportación visible si se necesita].

| Corte UML | Ruta del diagrama | Hash real del estado representado | Nuevas clases y relaciones relevantes | Test/escenario que motivó el cambio |
|---|---|---|---|---|
| Tras HU-01 | [enlace] | [...] | [...] | [...] |
| Tras HU-02 | [enlace] | [...] | [...] | [...] |
| Tras HU-03 | [enlace] | [...] | [...] | [...] |

**Cambio explicado en el vídeo:** [diagrama antes, diagrama después, hashes, responsabilidad trasladada, razón de la relación UML, pruebas de protección].

**Decisiones sobre herencia/composición:** [contrato de `Exercise` si existe, especializaciones y pruebas de sustitución; cuándo el combinado contiene componentes y por qué no implica necesariamente herencia]. Los diagramas deben corresponder al código de los hashes señalados y distinguir diseño hipotético del existente.

## 7. Dobles de prueba y colaboración

| Dependencia | Puerto / límite | Prueba y objetivo | Doble usado (stub / mock / fake) | Qué garantiza / qué NO demuestra | Test/commit |
|---|---|---|---|---|---|
| Persistencia | [...] | [...] | [...] | [...] | [...] |
| Proveedor IA | [...] | [...] | [...] | [...] | [...] |
| Otra, si procede | [...] | [...] | [...] | [...] | [...] |

**Aceptación integrada:** [qué pruebas usan reglas reales de dominio, dónde se verifica que una propuesta rechazada NO se guarda y qué colaboración se observa]. No confundir la verificación de `save(...)` con la recuperación en memoria.

## 8. Ejecución y evaluación de calidad final

**Cómo reproducir la entrega**, desde raíz del repositorio:

```powershell
mvn -f P4/pom.xml clean verify
```

[Comandos específicos para Cucumber/JUnit que realmente funcionan en el POM entregado. Explicar Eclipse: Run As → JUnit Test o Maven Build cuando proceda.]

| Métrica / resultado | Valor real y alcance | Fuente, fecha, commit | Interpretación crítica / riesgo | Decisión / cambio y reevaluación |
|---|---|---|---|---|
| JUnit: ejecutados, errores, omitidos | [...] | [Surefire/Actions] | [...] | [...] |
| Cucumber: escenarios ejecutados, fallos y pending | [...] | [reporte/Actions] | [...] | [...] |
| JaCoCo líneas / ramas (código nuevo y total, si distinguible) | [...] | [reporte y alcance] | [...] | [...] |
| Quality Gate CalSo | [...] | [SonarQube URL/fecha] | [...] | [...] |
| Bugs / vulnerabilidades / smells significativos | [...] | [regla, clase, severidad] | [...] | [...] |
| Duplicación / complejidad / mantenibilidad | [...] | [informe y alcance] | [...] | [...] |
| Hallazgo analizado antes y después | [...] | [dos enlaces/resultados] | [...] | [...] |

**Análisis final (150–250 palabras):** [escoger uno o dos resultados realmente significativos, por qué importan, qué se hizo o por qué no se modificó, resultado de regresión; reconocer que cobertura alta o Quality Gate verde no garantizan ausencia de errores. No inventar métricas no disponibles].

## 9. Evidencias y comprobación de entrega

- [ ] Enlaces de vídeo **individual** accesibles, con escenario, voz en tiempo real sincronizada con la interacción, pantalla/cursor legibles y duración máxima.
- [ ] Política de commits y técnica de auditoría descritas.
- [ ] Regresión final con tests JUnit y escenarios Cucumber reales; ningún Then vacío ni PENDING final.
- [ ] Tres versiones UML fieles a commits históricos y decisiones justificados.
- [ ] Documentación del uso de stubs/mocks/fakes y pruebas integradas.
- [ ] CI, JaCoCo y SonarQube enlazados y analizados críticamente.
- [ ] Incidencias, limitaciones y pendientes declarados.

**Nota:** no se valorará la cantidad de commits ni el número de clases por sí mismos; se evaluará la técnica, su aplicación reproducible, la calidad de las pruebas, la evolución del diseño y el análisis fundamentado de la calidad.
