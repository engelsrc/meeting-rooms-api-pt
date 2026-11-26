# Tareas — 000 agents-and-skills-create-project

Lote 0 local. Rama de trabajo: `feature/000-agents-and-skills-create-project` (tras existir `dev`).  
Sin Spring Boot / API en este RF.

## Tareas

- [x] **CMD-1** — Verify SDD artifacts and clock (RF-1, RF-5)  
  **Hecho cuando:** existen `.cursor/agents/constitution.md`, `.cursor/rules/constitution.mdc`, `task-lifecycle.mdc`, `git-workflow.mdc`, los 9 comandos `ssd-cmd-{create-rf,clarify,change,plan,task,implement,reviewer,close,status}.md`, `backend-developer.agent.md`; existe `PRUEBA-INICIO.md` con fecha/hora; la constitución menciona `PRUEBA-INICIO.md`; no se exige `.cursor/skills`.  
  **Hecho:** 2026-11-25 — verificación OK; t0 en `PRUEBA-INICIO.md`; sin `.cursor/skills`; solo carpeta spec `000-…`.

- [x] **CMD-2** — Confirm product RF gate and map (RF-6)  
  **Hecho cuando:** queda constancia verificable de que no se crearán `spec/001-…`…`008-…` hasta `close.md` de este `000`; el mapa de orden `001-persistence-migrations` → … → `008-delivery-readme` está en `rf.md` (o evidencia en el informe de review); no existen aún carpetas `spec/001-*` … `spec/008-*`.  
  **Hecho:** 2026-11-25 — gate y mapa en `rf.md` (RF-6); solo existe `spec/000-agents-and-skills-create-project`.

- [x] **GIT-1** — Initial commit on main (RF-2)  
  **Hecho cuando:** la rama actual/base es `main`; hay ≥1 commit en `main` que incluye `.cursor/`, `PRUEBA-INICIO.md` y `spec/000-agents-and-skills-create-project/`; `git status` no deja secretos staged; mensaje de commit en español.  
  **Hecho:** commit `37b2d40` en `main` — andamiaje SDD, reloj y RF 000 (+ `.gitignore`).

- [x] **GIT-2** — Create public GitHub repo and push main (RF-3)  
  **Hecho cuando:** existe repo público `meeting-rooms-api-pt` en la cuenta personal (`gh`); `git remote get-url origin` apunta a ese repo; `origin/main` existe tras `git push -u origin main`. Si ya hay otro `origin`, detener y preguntar (no forzar).  
  **Hecho:** https://github.com/engelsrc/meeting-rooms-api-pt (PUBLIC); `origin` OK; `origin/main` OK.

- [x] **GIT-3** — Create and push branch dev from main (RF-4)  
  **Hecho cuando:** se partió de `main`; existe rama local `dev` creada desde `main`; `origin/dev` existe tras push (`git push -u origin dev`).  
  **Hecho:** `dev` local + `origin/dev` desde `main` @ `37b2d40`.

- [x] **GIT-4** — Open feature branch for this RF (RF-7)  
  **Hecho cuando:** existe `feature/000-agents-and-skills-create-project` desde `dev` (o desde `main` solo si `dev` aún no estaba y el plan lo permite tras GIT-3); listo para commits restantes de este RF.  
  **Hecho:** rama `feature/000-agents-and-skills-create-project` creada desde `dev`.

- [x] **CMD-3** — Ready for reviewer and close (RF-7)  
  **Hecho cuando:** `plan.md` y `task.md` existen en esta carpeta; todas las casillas anteriores están hechas; se puede invocar `/ssd-cmd-reviewer` y, si cumple, `/ssd-cmd-close` (merge feature→`dev` local → push → pull → verificar → `close.md`).  
  **Hecho:** `plan.md` + `task.md` presentes; CMD-1…GIT-4 completas; listo para reviewer.

## Notas
- Prefijos: `CMD` = verificación/proceso; `GIT` = Git/GitHub.
- Una sola rama de feature para el RF; commits por tarea en esa rama tras GIT-4 (GIT-1…GIT-3 ocurren en `main`/`dev` según plan).
- Reloj: `PRUEBA-INICIO.md` ya es el t0; este RF no implementa la API.
