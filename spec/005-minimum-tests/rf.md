# RF — Suite mínima de tests

Estado: aprobada

## Metadatos
- Carpeta: `spec/005-minimum-tests/`
- Slug: `minimum-tests`
- Orden: `005`
- Tipo: `Test`
- Origen: change — clarify (JWT login HTTP, 3 clases, asserts GET, sin perfil test)
- Fecha: 2026-11-26
- GitHub Issue: #6 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/6

## Problema / oportunidad

El enunciado exige **al menos 3 tests**, uno de **integración** (MockMvc o Testcontainers). La API ya está en `dev`; falta evidencia automatizada mínima.

## Objetivo

Añadir **≥3 tests** con **`@SpringBootTest` + MockMvc + H2** (sin Testcontainers): create room autenticado, solape 409 y GET rooms público.

## Alcance

### Incluye
- **3 clases** de test separadas (`@SpringBootTest` + MockMvc cada una).
- H2 en memoria; **sin** `application-test.yml` / `@ActiveProfiles("test")`; properties en anotación si hacen falta.
- Auth en mutaciones: **`POST /api/auth/login`** (`admin`/`admin`) → `accessToken` → header `Authorization: Bearer …` (filtro JWT real; no `@WithMockUser` solo).
- Escenarios:
  1. Clase A: login + `POST /api/rooms` → **201**
  2. Clase B: login + sala + reserva ACTIVE + segunda reserva solapada (mismo JWT) → **409**
  3. Clase C: `GET /api/rooms` **sin** Authorization → **200** (solo status)
- Ejecutables con `mvn test`.

### Excluye
- Testcontainers; `@WithMockUser` como único auth; desactivar filtro JWT en tests.
- Perfil `test` dedicado / YAML test.
- Asserts de body en GET (solo status 200).
- Suite amplia; README (006); CI obligatorio.

## Actores
- Humano: aprueba; Issue open antes de implementar.
- Agente: escribe 3 clases y verifica `mvn test`.

## Requisitos funcionales

### RF-1 — Tres clases MockMvc
**CUANDO** se ejecuta `mvn test`, **EL SISTEMA** ejecuta ≥3 tests (en **3 clases**) que pasan con `@SpringBootTest` + MockMvc + H2.

#### Criterios de aceptación
- [ ] Tres clases de test distintas bajo `src/test/java`.
- [ ] Clase A: login seed → Bearer → `POST /api/rooms` válido → **201**.
- [ ] Clase B: login → crear sala → crear reserva ACTIVE → segundo create solapado (mismo Bearer) → **409**.
- [ ] Clase C: `GET /api/rooms` sin Authorization → **200** (sin exigir body).
- [ ] Sin `@ActiveProfiles("test")` obligatorio; H2 vía config existente o `@SpringBootTest` properties.
- [ ] Nombres de sala únicos por test para no chocar entre clases.
- [ ] `mvn test` verde de forma reproducible.

### RF-2 — Integración HTTP real
**CUANDO** se revise la suite, **EL SISTEMA** usa MockMvc contra la app (incl. filtro JWT en mutaciones), cumpliendo enunciado §6.

#### Criterios de aceptación
- [ ] Mutaciones pasan por el filtro JWT (Bearer de login), no solo `@WithMockUser`.
- [ ] Al menos (de hecho las tres) usan MockMvc.
- [ ] Sin Testcontainers.

## Reglas de negocio
- Enunciado §6: ≥3 + ≥1 MockMvc/Testcontainers → aquí 3 MockMvc + H2.
- Solape producto `[start,end)` ACTIVE → 409.
- Seed V4 `admin`/`admin` para login en tests A y B.

## Casos límite
- Aislamiento: nombres únicos; 3 contextos de clase (B).
- GET solo status 200 (lista vacía o no).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado.
- Mensajes API EN vía `ErrorMessages` (asserts de status suficientes en este RF).
- Perfiles `local`/`prod` de app; tests sin secretos reales.
- Chat/SDD en español; tests en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions post-clarify.

## Impacto en plan/task (aún no editar)
- No existen aún; crear tras APROBADO.

## Siguiente paso
`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/005-minimum-tests/rf.md`
