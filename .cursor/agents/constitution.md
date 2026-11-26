# Constitución — Pruebas técnicas

Canónica: `.cursor/agents/constitution.md`. Las rules y comandos deben leer **este** archivo.

**Vocabulario:** el **enunciado vigente** de la prueba es el de esta constitución (sección siguiente). Cada requisito de producto vive en `spec/<carpeta>/rf.md` (RF). No confundir enunciado con `rf.md`.

## Alcance
- Esta constitución orienta cómo trabajar pruebas técnicas en este repo.
- El **enunciado vigente** de cada prueba define requisitos, stack y plazos concretos. El de esta prueba está en la sección siguiente.
- Si el **enunciado vigente** y otras reglas del repo chocan, **manda el enunciado**. Los principios de ritmo/calidad de este documento no lo sustituyen; una **nueva prueba** reemplaza la sección de enunciado vigente.
- El **reloj de la prueba** queda marcado en el archivo raíz `PRUEBA-INICIO.md` cuando el humano lo cree/actualice. Mientras no exista ese marcador (o diga lo contrario), no se implementa la API.

## Enunciado vigente (prueba técnica)

Texto exacto del enunciado a cumplir:

El siguiente paso es una prueba técnica de no más de 3 horas:

Haz una API REST con Java 17 o superior y Spring Boot para reservar salas de reuniones.

1. Crear y listar salas. Crear, listar y cancelar reservas. Cada reserva tiene sala, responsable, inicio y fin.
2. Si la sala ya está ocupada en ese horario, devuelve 409. Validación de los datos con errores 400 claros.
3. Listado de reservas con filtro por sala y por fecha, con paginación.
4. Base de datos PostgreSQL o H2 con migraciones (Flyway o Liquibase).
5. Los endpoints que crean o cancelan requieren autenticación (JWT o Basic); los de consulta no.
6. Al menos 3 tests, uno de ellos de integración (MockMvc o Testcontainers).
7. Un README con cómo ejecutarlo, las decisiones que tomaste, qué harías con más tiempo y cómo usaste la IA (qué le pediste, qué aceptaste y qué corregiste).

Ve haciendo commits a medida que avanzas, para que se vea el progreso y cómo piensas; un solo commit con todo no vale.

Entrega: mándame por aquí el enlace a un repositorio público de GitHub en un plazo de 48 horas y dime cuánto tiempo te llevó. Si la prueba está bien, agendamos una entrevista de 20 minutos.

## Objetivo
- Cumplir el enunciado vigente dentro del tiempo que indique.
- Entregar algo ejecutable, claro y revisable (código + commits + README según pida el enunciado).
- Preferir lo suficiente y correcto frente a lo espectacular e incompleto.

## Idiomas
- Chat con el humano y documentos SDD (RF, plan, criterios de aceptación de issues, informes): **español**.
- Código, comentarios de código, logs y **mensajes de error de la API**: **inglés**.
- Git según `.cursor/rules/git-workflow.mdc`: al primer scaffold/kickoff **crear `dev`** si no existe; **una rama por RF** desde **`dev`** (inglés); **commits por tarea** en esa rama (español); **sin** merge/push hasta el close. Al **cerrar cada RF** (`/ssd-cmd-close`): **merge a `dev` local → push `origin/dev` → pull → verificar → `close.md`**. PR manuales (humano).

## Decisiones de repo (enunciado vigente)
Convenciones de **este** repo para la prueba actual (no sustituyen el enunciado; lo complementan). Una prueba nueva puede cambiarlas al reemplazar el enunciado y el agente:
- **Clean Architecture (CA)** + **Command Query Responsibility Segregation (CQRS)**: reglas completas (puertos/adaptadores, separación command/query); **módulos delgados** (sin bus de eventos, sin microservicios, sin infra de más) por el límite de **3 horas**.
- Mapeo **DTO ↔ dominio** (o DTO ↔ command/query).
- Mensajes de error de API vía clase de constantes (p. ej. `ErrorMessages`); **sin literales quemados**.
- Perfiles Spring **`local`** y **`prod`** (`application-local.yml` / `application-prod.yml`); base compartida sin secretos de prod en el repo.
- Entrega orientada a **commits frecuentes** por tarea y `dev` remoto al día al **cerrar cada RF**. **Lote 0 local** (`task.md`).
- Reloj: evidencia en `PRUEBA-INICIO.md` (raíz). Sin ese marcador, no implementar la API de salas.

## Tiempo y extras
- Prioridad absoluta: requisitos obligatorios del **enunciado vigente**.
- Evitar sobreingeniería temprana (p. ej. OpenAPI muy elaborado, Docker Compose complejo, u otras capas no pedidas) **mientras** falten requisitos. Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) = convención de repo en forma **delgada**: commands/queries + handlers claros, un solo módulo/app si basta.
- Si la implementación de lo obligatorio termina **antes** del tiempo de la prueba, **no rellenar con ruido**: definir e implementar mejoras o extras que aporten valor real al proyecto en el tiempo restante (documentarlas en el README y, si aplica, acordarlas con el humano antes de meterlas).
- Ejemplos de extras útiles (solo si sobra tiempo y encajan con el enunciado): documentación de API, Docker Compose sencillo, más tests, hardening, métricas básicas, DX (scripts; perfiles *adicionales* — `local`/`prod` ya son obligatorios). La lista no es cerrada.

## Ritmo de trabajo
- Avanzar por hitos pequeños y verificables.
- Commits frecuentes e intencionados que muestren progreso y criterio; un único commit con todo al final no vale (salvo que el enunciado diga lo contrario).
- Git (ramas, commits, merge): `.cursor/rules/git-workflow.mdc`.
- Ciclo por RF (create-rf → plan → task → implement con commits → review → `/ssd-cmd-close`: merge `dev` → push → pull → verificar): `.cursor/rules/task-lifecycle.mdc`.

## Calidad mínima (cuando el enunciado lo implique)
- Errores HTTP con significado claro (p. ej. validación vs conflicto vs no encontrado), sin inventar códigos que el enunciado no pedirá.
- Validación de entrada con mensajes entendibles **en inglés**, desde constantes (`ErrorMessages` o equivalente).
- Tests: al menos lo que pida el enunciado; si sobra tiempo, ampliar cobertura útil.
- README: cómo ejecutar, decisiones, qué harías con más tiempo, y cómo se usó la IA (qué se pidió, qué se aceptó, qué se corrigió), si el enunciado lo exige o es buena práctica de la entrega.

## Uso de IA
- Usar la IA para acelerar scaffolding, borradores y textos; no para entregar código sin revisión.
- Revisar siempre bordes del dominio, seguridad, persistencia y mensajes de error.
- Documentar el uso real de la IA en el README cuando la entrega lo pida.

## Anti-patrones
- Empezar el reloj o “dar por iniciada” la prueba sin confirmación del humano.
- Fijar stack/arquitectura **fuera** del enunciado vigente / decisiones de repo acordadas (al cambiar de prueba, actualizar enunciado + agente; no dejar restos contradictorios).
- Sobreingeniería antes de cerrar lo obligatorio (p. ej. event bus, read DB separada, o infra extra encima de Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado).
- Extras cosméticos que no aporten cuando aún faltan requisitos.
- Bloquear la implementación por no tener GitHub Issues (el kickoff es local).
- Commits con secretos o credenciales reales.
- Un solo commit monstruoso al final.
- Literales de mensaje de error en código; mensajes de API en español.
- Escribir solo «CA» o solo «CQRS» en docs de proceso: siempre **Clean Architecture (CA)** y **Command Query Responsibility Segregation (CQRS)**.
