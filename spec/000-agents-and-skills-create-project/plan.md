# Plan — 000 agents-and-skills-create-project

## Metadatos
- Carpeta: `spec/000-agents-and-skills-create-project/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-25
- Rama de trabajo prevista: `feature/000-agents-and-skills-create-project`

## Enfoque y orden

Meta **Git + verificación de andamiaje** (sin código de API). Orden:

1. Verificar artefactos SDD existentes (**RF-1**) y reloj (**RF-5**).
2. Documentar gate y mapa `001`…`008` (**RF-6**) en el propio `rf.md` / evidencia de review (sin crear carpetas producto).
3. Primer commit en **`main`** con `.cursor/`, `PRUEBA-INICIO.md`, `spec/000-…` (**RF-2**).
4. Crear repo público GitHub `meeting-rooms-api-pt`, configurar `origin`, push `main` (**RF-3**).
5. Crear `dev` desde `main`, push `dev` (**RF-4**).
6. Rama de feature del RF para commits de cierre SDD; review → close (**RF-7**).

Dependencias: RF-2 antes de RF-3; RF-3 antes de RF-4 (remoto); RF-7 cierra el ciclo tras lo anterior.

## Clean Architecture (CA) + CQRS

No aplica capas de aplicación en este RF (no hay Spring Boot). Se **reutiliza** la constitución/agente que ya exigen CA + Command Query Responsibility Segregation (CQRS) delgado, `ErrorMessages` y perfiles `local`/`prod` para RFs de producto posteriores. Sin event bus.

## Archivos a tocar

| Archivo / ruta | Responsabilidad |
|----------------|-----------------|
| `.cursor/agents/constitution.md` | Leer/verificar; no reescribir salvo hueco de reloj ya cubierto |
| `.cursor/rules/*` | Verificar |
| `.cursor/commands/ssd-cmd-*.md` | Verificar existencia |
| `.cursor/agents/backend-developer.agent.md` | Verificar |
| `PRUEBA-INICIO.md` | Verificar t0 |
| `spec/000-agents-and-skills-create-project/{rf,plan,task,review-*,close}.md` | Cadena SDD de este RF |
| `.git` / `origin` | Commits, remoto, ramas `main`/`dev` |
| *(no)* `src/**` Spring | Fuera de alcance |

## Algoritmo / procedimiento (pseudocódigo)

```text
ASSERT exists constitution, rules, commands, agent
ASSERT exists PRUEBA-INICIO.md with timestamp
ASSERT constitution references PRUEBA-INICIO.md
ASSERT no product folders 001..008 created in this RF

ON branch main (or create first commit on main):
  git add .cursor PRUEBA-INICIO.md spec/000-agents-and-skills-create-project
  git commit -m "chore: andamiaje SDD y marcador de reloj"
  # sin secretos

IF no origin OR origin wrong:
  gh repo create meeting-rooms-api-pt --public --source=. --remote=origin
  # o create + set-url; cuenta personal
ELSE IF origin points elsewhere:
  STOP and ask human

git push -u origin main
git checkout -b dev
git push -u origin dev

git checkout -b feature/000-agents-and-skills-create-project
# commits adicionales de task.md/plan evidencia si hace falta
# /ssd-cmd-reviewer → /ssd-cmd-close
# close: merge feature → dev local → push origin dev → pull → verify → close.md
```

## ErrorMessages / perfiles

No aplica en este RF (sin API). Quedan heredados para `001+`.

## Estrategia de pruebas

No suite JUnit. Verificación manual/comandos:

- `Test-Path` / listado de artefactos (**RF-1**, **RF-5**).
- `git log`, `git ls-files` (**RF-2**).
- `gh repo view meeting-rooms-api-pt`, `git remote -v`, `git branch -a` (**RF-3**, **RF-4**).
- Confirmación de ausencia de `spec/001-*`… hasta `close.md` (**RF-6**).

## Cobertura de requisitos

| Id | Enfoque en el plan |
|----|--------------------|
| **RF-1** | Checklist de existencia de constitución, rules, 9 comandos, agente; no exigir skills |
| **RF-2** | Commit inicial en `main` con paths acordados; sin secretos |
| **RF-3** | `gh repo create` público + `origin` + push `main` |
| **RF-4** | Partir de `main`; crear y pushear `dev` |
| **RF-5** | Verificar `PRUEBA-INICIO.md` + mención en constitución |
| **RF-6** | No crear `001`…`008`; mapa documentado en `rf.md`; gate = `close.md` |
| **RF-7** | Este `plan.md` + posterior `task.md`; feature branch; close según git-workflow |

## Fuera de alcance (heredado)

- Spring Boot / API / migraciones / auth / tests de producto / README de entrega.
- Skills SDD; Issues GitHub obligatorias; PRs automáticos.
- Otro nombre de repo o org distinta a cuenta personal.

## Riesgos

- `gh auth` fallido o sin permiso → RF-3/RF-4 bloqueados.
- `origin` preexistente distinto → detener (caso límite del RF).
- Commit monstruoso mezclando producto → prohibido; este RF solo andamiaje.
