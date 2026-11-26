---
description: >-
  Crea un RF bajo spec/{NNN}-{slug}/rf.md. Qué y porqué; hereda restricciones
  de la constitución. No plan, tareas ni código. No Git.
---

# ssd-cmd-create-rf

Crea un RF en `rf.md`. No escribas plan, tareas ni código. No ejecutes Git. El RF dice el **qué** y el **porqué**. No inventes librerías ni arquitectura fuera del enunciado / decisiones de repo.

## Antes de crear archivos

1. Lee `.cursor/agents/constitution.md`. Si hay conflicto con otras reglas, **manda el enunciado vigente** de la constitución. Si el requisito la contradice, detente y dilo; no crees la carpeta.
2. Lee, si aplica, `.cursor/rules/task-lifecycle.mdc` y `.cursor/agents/backend-developer.agent.md`.
3. Toma el contexto del comando: idea inicial y, si viene, nombre de carpeta.
4. Pregunta de una en una solo mientras quede una ambigüedad que **cambie el requisito** (casos límite, errores, fuera de alcance, para quién, resultado). Espera cada respuesta. Si el contexto ya alcanza, no preguntes y redacta.
5. **No** sugieras campos de auditoría (`create_by`, soft-delete, etc.) ni versionado histórico salvo que el humano o el enunciado lo pidan.
6. Busca en `spec/` el mayor prefijo numérico y usa el siguiente, con tres dígitos (`000` o `001` si no hay ninguna).
7. Propón el nombre de carpeta en **inglés** kebab-case: `{NNN}-{slug}`. Si el usuario ya pasó un nombre, úsalo con el número automático. Si es ambiguo, confirma antes de crear.

## Archivo

Crea un solo archivo: `spec/{NNN}-{slug}/rf.md`. Redáctalo breve, en **español**. Incluye la línea exacta: `Estado: borrador`. Afirma solo lo que el contexto y las respuestas permitan. No inventes reglas de negocio. Si un dato falta, escribe que queda pendiente de aclarar.

El `rf.md` deja explícitos:

- problema y objetivo;
- tipo orientativo según esta prueba: `DB` | `API` | `Model` | `Sec` | `Test` | `Docs` | `Meta` (u otro justificado);
- requisitos funcionales con id fijo `RF-1`, `RF-2`, … verificables (formas EARS u otras comprobables);
- fuera de alcance;
- criterios de aceptación;
- preguntas abiertas;
- **Restricciones heredadas** (si aplican): Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; mensajes de API en inglés vía `ErrorMessages`; perfiles Spring `local`/`prod`; sin event bus / infra de más en la ventana de 3 h.

No listes nombres de archivos de implementación ni diseño detallado de paquetes (eso va en `plan.md`).

## Al terminar

Si esperas una respuesta, no cierres con un comando. Si el `rf.md` quedó creado, cierra con esta línea. El comando va seguido del RF:

`Próxima acción: REVISAR EL RF, ejecutar /ssd-cmd-clarify spec/<carpeta>/rf.md`
