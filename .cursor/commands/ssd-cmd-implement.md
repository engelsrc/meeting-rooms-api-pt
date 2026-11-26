---
description: Implementa una tarea pendiente (commits en rama; sin merge ni push)
---

# ssd-cmd-implement

Implementa **una** sola tarea pendiente. No crees tareas nuevas ni amplíes el plan.  
**Solo commits** en la rama de trabajo (español). **No** hagas merge a `dev`, **no** hagas push. La integración a `dev` es al final del RF en `/ssd-cmd-close`.

## Antes de implementar

1. Lee `.cursor/agents/constitution.md`. Si el **reloj no ha arrancado** (humano no lo indicó), detente: no implementes la API de salas.
2. Lee `.cursor/rules/git-workflow.mdc` y `.cursor/rules/task-lifecycle.mdc`.
3. El comando debe incluir la carpeta del RF. Si falta, detente.
4. Lee `spec/<carpeta>/rf.md`, `plan.md` y `task.md`. Si falta alguno, detente.
5. Si `dev` no existe: créala desde `main`/HEAD **solo** si el humano ya arrancó el reloj o pidió scaffold/kickoff; si no, detente y dilo.
6. **Una rama por RF** (carpeta): `feature/<carpeta>` o `feature/<slug>` desde `dev` (inglés). Si la rama del RF ya existe, úsala; no crees una rama por tarea. Si no está claro `feature/` vs `fix/`, pregunta y espera.
7. Toma la primera tarea pendiente de `task.md` (orden del plan). Si no queda ninguna, detente.
8. **Issue del RF (gate):** debe existir `GitHub Issue: #N` en `rf.md`/`task.md` y estar **open** en GitHub **antes** de cualquier cambio de código. Si falta, créala primero (`gh issue create`, estado open) y anótala; si está closed, detente. No crees una Issue por tarea. Sin Issue **open** → no implementes.

## Ejecución

- Código y mensajes de error de API en **inglés**; sin literales quemados → `ErrorMessages`.
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado: mutaciones vía command, lecturas vía query; sin event bus.
- Solo esa tarea, dentro de spec + plan. Si no se puede cumplir, detente y dilo.
- Si el plan exige prueba para ese comportamiento: escribe primero la prueba (falla), luego el código hasta que pase. Muestra el resultado.
- Si la tarea toca HTTP: cumple método, ruta, respuesta y auth del «Hecho cuando».
- Marca la casilla en `task.md` solo si se cumple el «Hecho cuando». Indica el `RF`. Una tarea cumplida no se repite. Al terminar esta, **para**.
- Commits atómicos en la **rama del RF** (español), uno o más por tarea. **Sin** merge a `dev`. **Sin** `git push`.

## Al terminar

Si quedan tareas pendientes:

`Próxima acción: IMPLEMENTAR LA SIGUIENTE, ejecutar /ssd-cmd-implement spec/<carpeta>/rf.md`

Si no quedan:

`Próxima acción: REVISAR EL RESULTADO, ejecutar /ssd-cmd-reviewer spec/<carpeta>/rf.md`

Si te detuviste porque la tarea no se puede cumplir, no inventes la acción siguiente.
