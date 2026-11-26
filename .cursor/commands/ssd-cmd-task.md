---
description: Divide plan.md en tareas en task.md y crea 1 Issue GitHub del RF
---

# ssd-cmd-task

Divide el plan en tareas pequeñas y verificables. No escribas código.  
**GitHub:** crea **una Issue por RF** en estado **open** (no una por tarea). Obligatoria **antes** de implementar. El desglose vive en `task.md`.

## Antes de crear archivos

1. Lee `.cursor/agents/constitution.md` y `.cursor/rules/task-lifecycle.mdc`. Si una tarea contradijera la constitución, detente y dilo.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md` y `plan.md`. Si falta alguno, detente.
4. Si `plan.md` está obsoleto, detente: primero `/ssd-cmd-plan`.
5. Si ya existe `task.md` y **no** está marcado obsoleto, detente y dilo. No lo sobrescribas. (Si solo falta la Issue, créala y anótala; no regeneres tareas.)
6. Si `task.md` está **obsoleto**, puedes **sobrescribirlo**.
7. Si el plan no cabe en ~10 tareas, detente y propone dividirlo en bloques. No crees el archivo hasta que confirmen el corte.

## Archivo `task.md`

Crea o **reemplaza** `spec/<carpeta>/task.md` (español) si estaba obsoleto. Tareas del plan y de los AC, en orden de dependencia. No dejes la línea `Estado: obsoleto` en el task nuevo. En esta API: migraciones/DB → application/API → seguridad en mutaciones → tests → docs, según lo que diga el plan. No inventes tareas fuera del plan.

Cada tarea lleva:

- casilla `- [ ]`;
- prefijo + consecutivo: `DB-n`, `API-n`, `SEC-n`, `TEST-n`, `DOCS-n`, `MODEL-n`, `CMD-n` (u otro tipo del RF);
- el `RF` que cubre;
- línea **Hecho cuando:** con comprobación verificable.

Si la tarea crea o cambia una operación HTTP, el «Hecho cuando» incluye método, ruta, respuesta esperada (y auth sí/no). Si un dato no alcanza, escribe que queda pendiente de aclarar.

Títulos pensados para ramas: en **inglés** (kebab-case); descripción de la tarea en español.

## GitHub Issue (obligatoria — 1 por RF, estado open)

Tras escribir `task.md` (o si ya existía y solo faltaba la Issue):

1. Si `rf.md` ya tiene `GitHub Issue: #N` y la Issue existe en estado **open**, **no** crees otra.
2. Si la Issue está **closed** y el RF aún no tiene `close.md`, detente y dilo (no reabrir sin decisión del humano).
3. Si no hay Issue: crea **una** en el repo `origin` con `gh issue create` (GitHub la deja en **open**):
   - **Título:** `[RF NNN] <slug o título corto del RF>` (inglés o español breve).
   - **Cuerpo** (español): carpeta `spec/<carpeta>/`, objetivo del RF, lista corta de AC / ids `RF-*`, rama prevista `feature/<carpeta>`, nota de que el detalle está en `task.md`.
   - Labels opcionales: `rf` si existen.
4. Anota en `rf.md` (metadatos): `GitHub Issue: #N` y la URL.
5. Anota al inicio de `task.md`: `GitHub Issue: #N` (estado **open** hasta `/ssd-cmd-close`).

Si `gh` / `origin` no están disponibles, detente, dilo y no finjas la Issue. **No** pases a implementar sin Issue **open**.

## Al terminar

Si esperas el corte de bloques:

`Próxima acción: CONFIRMAR EL CORTE de spec/<carpeta>/rf.md.`

Si `task.md` y la Issue quedaron creados:

`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/<carpeta>/rf.md`

**Nota reloj:** no implementar la API hasta que el humano arranque el reloj (constitución).
