---
description: Divide plan.md en tareas pequeñas en task.md (sin código)
---

# ssd-cmd-task

Divide el plan en tareas pequeñas y verificables. No escribas código. No ejecutes Git. **Lote 0 local:** las tareas viven en `task.md` (no hace falta crear GitHub Issues).

## Antes de crear archivos

1. Lee `.cursor/agents/constitution.md` y `.cursor/rules/task-lifecycle.mdc`. Si una tarea contradijera la constitución, detente y dilo.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md` y `plan.md`. Si falta alguno, detente.
4. Si `plan.md` está obsoleto, detente: primero `/ssd-cmd-plan`.
5. Si ya existe `task.md` y **no** está marcado obsoleto, detente y dilo. No lo sobrescribas.
6. Si `task.md` está **obsoleto**, puedes **sobrescribirlo**.
7. Si el plan no cabe en ~10 tareas, detente y propone dividirlo en bloques. No crees el archivo hasta que confirmen el corte.

## Archivo

Crea o **reemplaza** `spec/<carpeta>/task.md` (español) si estaba obsoleto. Tareas del plan y de los AC, en orden de dependencia. No dejes la línea `Estado: obsoleto` en el task nuevo. En esta API: migraciones/DB → application/API → seguridad en mutaciones → tests → docs, según lo que diga el plan. No inventes tareas fuera del plan.

Cada tarea lleva:

- casilla `- [ ]`;
- prefijo + consecutivo: `DB-n`, `API-n`, `SEC-n`, `TEST-n`, `DOCS-n`, `MODEL-n`, `CMD-n` (u otro tipo del RF);
- el `RF` que cubre;
- línea **Hecho cuando:** con comprobación verificable.

Si la tarea crea o cambia una operación HTTP, el «Hecho cuando» incluye método, ruta, respuesta esperada (y auth sí/no). Si un dato no alcanza, escribe que queda pendiente de aclarar.

Títulos pensados para ramas: en **inglés** (kebab-case); descripción de la tarea en español.

## Al terminar

Si esperas el corte de bloques:

`Próxima acción: CONFIRMAR EL CORTE de spec/<carpeta>/rf.md.`

Si `task.md` quedó creado:

`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/<carpeta>/rf.md`

**Nota reloj:** no implementar la API hasta que el humano arranque el reloj (constitución).
