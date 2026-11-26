---
description: Crea plan.md técnico de un RF aprobado (sin tareas ni código)
---

# ssd-cmd-plan

Crea el plan de un RF aprobado. No escribas tareas ni código. No ejecutes Git.

## Antes de crear archivos

1. Lee `.cursor/agents/constitution.md` (y `task-lifecycle.mdc` / agente backend si aplica). Si el plan contradijera la constitución, detente y dilo.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md`. Si no existe, detente.
4. Si la línea de estado no es exactamente `Estado: aprobada`, o quedan preguntas abiertas / datos pendientes, detente y dilo. No crees el plan.
5. Si ya existe `plan.md` y **no** está marcado `Estado: obsoleto — regenerar tras cambio del RF`, detente y dilo. No lo sobrescribas.
6. Si `plan.md` está **obsoleto** (o el humano pide recrear tras `/ssd-cmd-change`): puedes **sobrescribir** `plan.md`. Si existe `task.md`, márcalo obsoleto con la misma línea de estado (o bórralo solo si el humano lo confirma); las tareas se recrean con `/ssd-cmd-task`.

## Archivo

Crea o **reemplaza** `spec/<carpeta>/plan.md` (español) según las reglas de arriba. Afirma solo lo que el `rf.md` permite. Cubre cada `RF` y marca ese identificador. No dejes la línea `Estado: obsoleto` en el plan nuevo.

El plan deja explícitos:

- enfoque y orden de implementación (respetar dependencias; en esta prueba suele ir DB → API → auth → tests → docs);
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) **delgado**: capas/paquetes previstos, command vs query, sin event bus;
- archivos a crear/modificar y responsabilidad de cada uno;
- algoritmo / reglas en pseudocódigo (p. ej. solape → 409);
- mensajes de error vía `ErrorMessages` (inglés);
- perfiles `local` / `prod` si toca config;
- estrategia de pruebas (≥3 tests, ≥1 integración si es el RF de tests o al acumular);
- fuera de alcance.

No inventes alcance nuevo. Si un dato falta, escribe que queda pendiente de aclarar.

## Al terminar

Si te detuviste porque el `rf.md` no está en `Estado: aprobada`, cierra con:

`Próxima acción: APROBAR EL RF. Cambiar la línea a Estado: aprobada en spec/<carpeta>/rf.md.`

Si el plan quedó creado, cierra con:

`Próxima acción: CREAR LAS TAREAS, ejecutar /ssd-cmd-task spec/<carpeta>/rf.md`
