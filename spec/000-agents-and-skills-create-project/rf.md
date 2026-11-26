# RF — Andamiaje SDD + GitHub (meta create-project)

Estado: aprobada

## Metadatos
- Carpeta: `spec/000-agents-and-skills-create-project/`
- Slug: `agents-and-skills-create-project`
- Orden: `000`
- Tipo: `Meta` | `Git` | `Comando`
- Origen: change tras clarify; reconstrucción del alcance meta
- Fecha: 2026-11-25

## Problema / oportunidad

Sin andamiaje SDD versionado y sin remoto GitHub (`main`/`dev`), la prueba de 3 h no tiene base trazable ni cumple el ritmo de commits/entrega del enunciado.

## Objetivo

Dejar **listo y en control de versiones** el proceso Cursor (constitución, comandos, rules, agente) y el repositorio GitHub público `meeting-rooms-api-pt` (partir de `main`, crear `dev`), **antes** de abrir cualquier RF de producto `001`…`008`.

## Alcance

### Incluye
- Usar los artefactos **ya definidos** bajo `.cursor/` (no reinventarlos si existen):
  - constitución: `.cursor/agents/constitution.md` (+ puntero `rules/constitution.mdc`);
  - comandos: `.cursor/commands/ssd-cmd-*.md`;
  - rules: `task-lifecycle.mdc`, `git-workflow.mdc`;
  - agente: `backend-developer.agent.md`.
- Incluir esos artefactos (y este RF / `PRUEBA-INICIO.md`) en **control de versiones**.
- Crear repositorio **público** en GitHub: `meeting-rooms-api-pt` en la **cuenta personal** del humano (`gh auth`).
- Comprobar que el trabajo parte de la rama **`main`**.
- Crear la rama **`dev`** desde `main` y dejar `origin` configurado (`main` y `dev` en remoto según el flujo acordado).
- Marcador de reloj: `PRUEBA-INICIO.md` en la raíz (evidencia de t0).
- Documentar el mapa de RF de producto `001`…`008` (solo orden; **no** crear esas carpetas en este RF).

### Excluye
- Scaffold Spring Boot / código de la API (va en `001`…).
- Crear o restaurar **skills** SDD: si no existen, no se crean; el flujo es **comandos**. Si en el futuro aparecen skills, no son gate de este RF.
- Abrir PR automáticamente.
- GitHub Issues obligatorias (Lote 0 = `task.md`).
- Crear carpetas `spec/001-…` … `008-…` en este RF.
- Empezar RF `001` antes de que este `000` tenga `close.md`.

## Actores
- Humano: aprueba RF, autentica `gh`, PRs manuales.
- Agente: ejecuta tareas del plan/task de este meta (Git + verificación de artefactos).

## Requisitos funcionales

### RF-1 — Artefactos SDD existentes en el repo
**CUANDO** se revise el andamiaje, **EL SISTEMA** dispone de constitución, comandos `ssd-cmd-*`, rules de ciclo/Git y agente backend ya definidos y alineados al enunciado vigente.

#### Criterios de aceptación
- [ ] Existen `.cursor/agents/constitution.md` y `.cursor/rules/constitution.mdc` (puntero).
- [ ] Existen `task-lifecycle.mdc` y `git-workflow.mdc`.
- [ ] Existen comandos: create-rf, clarify, change, plan, task, implement, reviewer, close, status.
- [ ] Existe `.cursor/agents/backend-developer.agent.md` apuntando a `/ssd-cmd-implement` y a la constitución.
- [ ] No se exige carpeta `.cursor/skills` para cumplir este RF.

### RF-2 — Control de versiones de andamiaje
**CUANDO** se complete el versionado inicial, **EL SISTEMA** tiene los artefactos de `.cursor/`, `spec/000-…` y `PRUEBA-INICIO.md` rastreados por Git (sin secretos).

#### Criterios de aceptación
- [ ] Hay al menos un commit en `main` que incluye `.cursor/`, `PRUEBA-INICIO.md` y `spec/000-agents-and-skills-create-project/`.
- [ ] No se commitean secretos ni credenciales reales.

### RF-3 — GitHub público `meeting-rooms-api-pt`
**CUANDO** se publique el remoto, **EL SISTEMA** tiene un repositorio público `meeting-rooms-api-pt` en la cuenta personal del humano, con `origin` apuntando a él.

#### Criterios de aceptación
- [ ] Existe el repo público `meeting-rooms-api-pt` (cuenta personal / `gh`).
- [ ] `git remote origin` apunta a ese repo.
- [ ] `main` está en el remoto (push inicial hecho).

### RF-4 — Partir de `main` y crear `dev`
**CUANDO** se prepare la integración, **EL SISTEMA** verifica que la base es `main` y crea `dev` desde `main`.

#### Criterios de aceptación
- [ ] Se comprueba que se parte de `main` (rama actual o base del primer commit = `main`).
- [ ] Existe rama local `dev` creada desde `main`.
- [ ] `dev` está en `origin` (`git push -u origin dev` o equivalente).

### RF-5 — Reloj
**CUANDO** el humano marca el inicio, **EL SISTEMA** deja t0 en `PRUEBA-INICIO.md` y la constitución lo reconoce como evidencia.

#### Criterios de aceptación
- [ ] Existe `PRUEBA-INICIO.md` en la raíz con fecha y hora.
- [ ] La constitución referencia `PRUEBA-INICIO.md` como evidencia del reloj.
- [ ] Criterio único para implementar API: existe ese marcador (no hace falta otra “confirmación” ambigua).

### RF-6 — Gate antes de producto
**MIENTRAS** este RF `000` no tenga `close.md` con review cumple, **EL SISTEMA** (proceso) no crea carpetas `spec/001-…` ni implementa la API de salas como RF de producto.

#### Criterios de aceptación
- [ ] Queda explícito: **no** abrir `001`…`008` hasta `close.md` de este `000`.
- [ ] El mapa de orden de producto queda documentado: `001-persistence-migrations` → `002-rooms-create-list` → `003-reservations-crud` → `004-overlap-409-validation-400` → `005-reservations-filters-pagination` → `006-auth-mutations` → `007-minimum-tests` → `008-delivery-readme`.

### RF-7 — Cadena SDD de este meta
**EL SISTEMA** trata este `000` con la misma cadena: plan → task → implement (commits en `feature/000-agents-and-skills-create-project`) → reviewer → close (merge a `dev` local → push → pull → verificar).

#### Criterios de aceptación
- [ ] Existen `plan.md` y `task.md` de este RF tras `/ssd-cmd-plan` y `/ssd-cmd-task`.
- [ ] El cierre sigue `/ssd-cmd-close` tras review cumple.

## Reglas de negocio
- Reutilizar artefactos existentes; no duplicar constitución/comandos “desde cero” si ya cumplen.
- Repo GitHub: público, nombre fijo `meeting-rooms-api-pt`, dueño = cuenta personal.
- Una rama de trabajo por este RF; integración a `dev` solo en close.
- Lote 0 local en `task.md`.

## Casos límite
- Si `PRUEBA-INICIO.md` se borra: recrearlo antes de cualquier `/ssd-cmd-implement` de API.
- Si falla la creación del repo remoto: no marcar RF-3/RF-4 como hechos; documentar error y reintentar.
- Si ya existiera un `origin` distinto: detenerse y pedir decisión al humano (fuera del feliz path de este RF).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado (para RFs de producto posteriores).
- Mensajes de API en inglés vía `ErrorMessages`; perfiles `local`/`prod` (producto).
- Chat/SDD en español; sin secretos en Git.
- Sin event bus / infra de más en la ventana de 3 h.

## Preguntas abiertas
- Ninguna tras AskQuestions del change (carpeta A; artefactos D; GitHub A; nombre A; dueño A; sin Spring Boot A; gate close antes de 001 A).

## Siguiente paso
Tras **APROBADO**: `Estado: aprobada` → `/ssd-cmd-plan spec/000-agents-and-skills-create-project/rf.md`.
