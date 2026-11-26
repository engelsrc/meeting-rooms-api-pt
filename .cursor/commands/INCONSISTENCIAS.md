# Auditoría de inconsistencias — **CERRADA**

Fecha cierre: 2026-11-25.  
Fuente: `.cursor/agents/constitution.md` + comandos `ssd-cmd-*` + rules + agente.

## Acuerdos

| Tema | Decisión |
|------|----------|
| Git | Commits por tarea; al close del RF: merge `dev` local → push → pull → verificar → cerrar Issue → `close.md` |
| Ramas | Una rama `feature/<carpeta>` por RF |
| Issues | **Una** por RF: **open** en `/ssd-cmd-task` antes de implementar; **closed** en `/ssd-cmd-close` |
| Artefacto RF | `spec/<carpeta>/rf.md` |
| Crear RF | `/ssd-cmd-create-rf` |
| Lenguaje | enunciado vigente ≠ RF; comandos dicen RF |
| Skills | Eliminadas; solo comandos |

## Cadena

```text
/ssd-cmd-create-rf → /ssd-cmd-clarify → (/ssd-cmd-change)
  → Estado: aprobada → /ssd-cmd-plan → /ssd-cmd-task
  → /ssd-cmd-implement → /ssd-cmd-reviewer → /ssd-cmd-close
```

## Nits (resueltos 2026-11-25)

- Constitución §Ritmo: ciclo por RF (no “por ítem” con merge en cada uno).
- Agente: “la spec” → enunciado / RF / plan.
- `git-workflow`: título “Política Git (por RF)” (sin “opción B”).

## Siguiente

Cuando el humano lo pida: `/ssd-cmd-create-rf` (reloj de la API sigue parado hasta confirmación explícita).
