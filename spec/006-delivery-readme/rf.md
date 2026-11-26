# RF — README de entrega

Estado: aprobada

## Metadatos
- Carpeta: `spec/006-delivery-readme/`
- Slug: `delivery-readme`
- Orden: `006`
- Tipo: `Docs`
- Origen: change — AskQuestions (tabla endpoints, IA por bullets humano, slug fijo, tiempo en última task)
- Fecha: 2026-11-26
- GitHub Issue: #7 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/7

## Problema / oportunidad

El enunciado exige un **README** de entrega. La API, persistencia, auth y tests mínimos ya están en `dev`; falta el documento raíz que explique ejecución, decisiones, backlog con más tiempo y uso real de la IA.

## Objetivo

Entregar un `README.md` en la raíz del repo (español) que cumpla el enunciado §7 y permita a un revisor arrancar y entender la prueba sin leer todo el código.

## Alcance

### Incluye
- Archivo **`README.md`** en la raíz del repositorio.
- Secciones obligatorias del enunciado:
  1. **Cómo ejecutarlo**: Java/Maven, perfiles `local`/`prod`, arranque, placeholders de env sin secretos reales, URL base `http://localhost:8080`, login seed, **flujo mínimo curl** (login → mutación Bearer → GET público) **y tabla breve de todos los endpoints** (método, ruta, auth sí/no).
  2. **Decisiones** (stack, Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado, H2/Flyway, JWT, solape `[start,end)`, etc.).
  3. **Qué harías con más tiempo** (mejoras reales, priorizables).
  4. **Cómo usaste la IA**: el **humano aporta bullets** (qué pidió, qué aceptó, qué corrigió); el **agente solo formatea** (no inventa hechos).
  5. **Tiempo**: sección corta actualizada en la **última tarea** del RF (t0 desde `PRUEBA-INICIO.md` + duración al cierre de este RF); no sustituye ese archivo.
- Enlace al repo público: https://github.com/engelsrc/meeting-rooms-api-pt
- Carpeta SDD canónica: **`spec/006-delivery-readme/`** (no renombrar).

### Excluye
- OpenAPI/Swagger elaborado; Docker Compose (salvo mención como “más tiempo”).
- Reescritura de código de producto o nuevos endpoints.
- Ampliar la suite de tests.
- Traducción EN del README (entrega en español).
- CI obligatorio; publicar secretos reales.
- Que el agente invente el apartado IA sin bullets del humano.

## Actores
- Humano: aprueba RF; **entrega bullets del apartado IA** antes o durante la task correspondiente; puede revisar tono.
- Agente: redacta README alineado a `dev`; formatea bullets IA; actualiza tiempo en la última task.
- Revisor externo: usa el README para ejecutar y evaluar.

## Requisitos funcionales

### RF-1 — README ejecutable
**CUANDO** un revisor abre el repo, **EL SISTEMA DE DOCUMENTACIÓN** (README raíz) le permite arrancar la API, ver el mapa de endpoints y probar un flujo mínimo autenticado.

#### Criterios de aceptación
- [ ] Existe `README.md` en la raíz.
- [ ] Indica requisitos (Java ≥17, Maven) y cómo arrancar (`mvn …` / perfil).
- [ ] Indica perfil por defecto / `local` vs `prod`; documenta placeholders de env para `local` (URL DB, usuario, JWT secret demo) **sin** secretos reales.
- [ ] Ejemplos curl usan base **`http://localhost:8080`**.
- [ ] Incluye login seed documentado en V4 (`admin`/`admin`) y flujo mínimo: login → mutación con Bearer → consulta pública.
- [ ] Incluye **tabla breve de todos los endpoints** (método, ruta, auth sí/no).
- [ ] Incluye cómo ejecutar tests (`mvn test`).

### RF-2 — Decisiones, más tiempo e IA
**CUANDO** se lea el README, **EL DOCUMENTO** explica decisiones de diseño, backlog con más tiempo y uso real de la IA (hechos aportados por el humano).

#### Criterios de aceptación
- [ ] Sección de **decisiones** coherente con lo implementado (CA+CQRS delgado, H2+Flyway, JWT, 409 solape, etc.).
- [ ] Sección **qué haría con más tiempo** con ≥3 ítems concretos y priorizables.
- [ ] Sección **uso de la IA** basada **solo** en bullets del humano (qué pidió / aceptó / corrigió); agente formatea, no inventa.
- [ ] Idioma del README: **español**.
- [ ] Sin credenciales/secretos de producción.

### RF-3 — Tiempo de la prueba en README
**CUANDO** se complete la última tarea de este RF, **EL README** refleja el tiempo orientativo de la prueba.

#### Criterios de aceptación
- [ ] Existe sección corta **Tiempo** (o equivalente) con t0 referenciado a `PRUEBA-INICIO.md`.
- [ ] La **última tarea** del RF actualiza la duración (o marca de cierre) antes del review/close.
- [ ] No sustituye ni borra `PRUEBA-INICIO.md`.

## Reglas de negocio
- Enunciado §7: README con ejecución, decisiones, más tiempo, uso de IA.
- El README describe el estado en `dev` al momento de este RF; no inventa features no entregadas.
- Bullets IA = fuente de verdad del humano; sin bullets → no se cierra la task de IA.

## Casos límite
- Puerto distinto de 8080: documentar el default del proyecto; si cambia en código, alinear README.
- Perfil `local` sin Postgres: README indica qué vars hacen falta y que `prod`/default H2 sirve para demo rápida.
- Humano aún no envió bullets IA: la task de formateo queda bloqueada hasta recibirlos.
- Duración final: se calcula/actualiza en la última task (no inventar horas en tasks previas).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado (documentar, no reintroducir event bus).
- Mensajes de API en inglés vía `ErrorMessages` (el README puede citarlo).
- Perfiles Spring `local` / `prod`.
- Chat/SDD en español; código/errores API en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.
- Sin sobreingeniería documental en la ventana de 3 h.

## Preguntas abiertas
- Ninguna tras AskQuestions (change).

## Impacto en plan/task (aún no editar)
- Aún no existen `plan.md` / `task.md`: crear tras APROBADO con `/ssd-cmd-plan` y `/ssd-cmd-task`.
- El plan deberá incluir: (1) estructura README + ejecución/tabla/flujo, (2) decisiones + más tiempo, (3) formateo IA **tras** bullets del humano, (4) **última task** = actualizar sección Tiempo.
- Si más adelante existieran plan/task y este RF cambia otra vez: marcarlos obsoletos y regenerar.

## Siguiente paso
`Próxima acción: REVISAR EL RESULTADO, ejecutar /ssd-cmd-reviewer spec/006-delivery-readme/rf.md`
