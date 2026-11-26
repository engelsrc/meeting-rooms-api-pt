# Tareas — 006 delivery-readme

GitHub Issue: #7 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/7 (open)  
Rama: `feature/006-delivery-readme`  
README raíz ES; bullets IA del humano; última task = Tiempo.

## Tareas

- [x] **DOCS-1** — README skeleton (RF-1, RF-2, RF-3)  
  **Título (EN):** `[docs] Create README section skeleton`  
  **Hecho cuando:** existe `README.md` en la raíz con títulos: Cómo ejecutarlo, Endpoints, Flujo mínimo, Tests, Decisiones, Qué haría con más tiempo, Cómo usé la IA (placeholder), Tiempo (placeholder), Repo; sin inventar bullets IA ni duración final.  
  **Hecho:** 2026-11-26 — `README.md` con secciones placeholder.

- [x] **DOCS-2** — Runbook + endpoints + curl (RF-1)  
  **Título (EN):** `[docs] Document runbook endpoints and curl`  
  **Hecho cuando:** sección ejecución con Java ≥17, Maven, perfiles `local`/`prod`, placeholders sin secretos reales, base `http://localhost:8080`; tabla de todos los endpoints (método, ruta, auth); flujo curl login `POST /api/auth/login` → `POST /api/rooms` (Bearer, 201) → `GET /api/rooms` (200, auth no); documentado `mvn test`.  
  **Hecho:** 2026-11-26 — runbook + tabla + curl + tests en `README.md`.

- [ ] **DOCS-3** — Decisions and future work (RF-2)  
  **Título (EN):** `[docs] Write decisions and more-time section`  
  **Hecho cuando:** sección decisiones alineada a `dev` (CA+CQRS delgado, H2/Flyway, JWT, solape 409, `ErrorMessages` EN); sección “más tiempo” con ≥3 ítems concretos; idioma español.

- [ ] **DOCS-4** — Format IA bullets from human (RF-2)  
  **Título (EN):** `[docs] Format human AI-usage bullets`  
  **Dependencia:** bullets del humano recibidos en el chat (sin ellos → no marcar hecha).  
  **Hecho cuando:** sección “Cómo usé la IA” formatea solo esos bullets (qué pedí / acepté / corregí); agente no inventa hechos.

- [ ] **DOCS-5** — Update elapsed time (RF-3) — **última**  
  **Título (EN):** `[docs] Update README elapsed time`  
  **Hecho cuando:** sección Tiempo con t0 referenciado a `PRUEBA-INICIO.md` y duración actualizada al cierre de este RF; no modifica/borra `PRUEBA-INICIO.md`.
