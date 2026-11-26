# Review 001 — 005 minimum-tests

Fecha: 2026-11-26  
RF: `spec/005-minimum-tests/rf.md`  
Issue: #6 (OPEN) — https://github.com/engelsrc/meeting-rooms-api-pt/issues/6  
Rama de trabajo (referencia): `feature/005-minimum-tests`

## Veredicto

**cumple**

## RF-1 — Tres clases MockMvc

| Prueba del plan | Evidencia | Resultado |
|-----------------|-----------|-----------|
| `CreateRoomAuthTest` (A) | `src/test/java/com/meetingrooms/api/CreateRoomAuthTest.java`: `@SpringBootTest` + `@AutoConfigureMockMvc`; `AuthTestSupport.loginAdmin` → Bearer → `POST /api/rooms` → `isCreated()`; nombre `Test-Room-` + UUID | cumple |
| `ReservationOverlapTest` (B) | `ReservationOverlapTest.java`: login → create room → 1ª reserva 201 → 2ª solapada `isConflict()` (409); mismo Bearer | cumple |
| `ListRoomsPublicTest` (C) | `ListRoomsPublicTest.java`: `GET /api/rooms` sin Authorization → `isOk()` (200); sin assert de body | cumple |
| `mvn test` | Suite exit 0 tras renombre `*Test` (3 clases + helper) | cumple |

### Criterios de aceptación RF-1

| Criterio | Evidencia / hueco |
|----------|-------------------|
| Tres clases distintas bajo `src/test/java` | Sí: `CreateRoomAuthTest`, `ReservationOverlapTest`, `ListRoomsPublicTest` |
| Clase A: login → Bearer → POST rooms → 201 | Sí (método `createRoomWithJwtReturns201`) |
| Clase B: login → sala → reserva → solape → 409 | Sí (intervalos solapados `[10:00,11:00)` vs `[10:30,11:30)`) |
| Clase C: GET rooms sin auth → 200 | Sí |
| Sin `@ActiveProfiles("test")`; H2 vía config existente | Sin `@ActiveProfiles` / `application-test` en `src/test`; contexto usa perfil app (H2 mem en log) |
| Nombres de sala únicos | UUID en A y B |
| `mvn test` verde | Exit 0 reproducible |

## RF-2 — Integración HTTP real

| Prueba del plan | Evidencia | Resultado |
|-----------------|-----------|-----------|
| Bearer vía login real | `AuthTestSupport.loginAdmin`: `POST /api/auth/login` admin/admin → `accessToken`; A/B usan header Bearer | cumple |
| MockMvc en las tres | Las 3 clases inyectan `MockMvc` | cumple |
| Sin Testcontainers | Sin deps/usos Testcontainers en tests | cumple |

### Criterios de aceptación RF-2

| Criterio | Evidencia / hueco |
|----------|-------------------|
| Mutaciones por filtro JWT (no solo `@WithMockUser`) | Login HTTP + Bearer; sin `@WithMockUser` en `src/test` |
| Al menos (las tres) usan MockMvc | Sí |
| Sin Testcontainers | Sí |

## Contraste con constitución

| Punto | Estado |
|-------|--------|
| Enunciado §6 (≥3 tests, ≥1 integración MockMvc/Testcontainers) | Cubierto: 3 MockMvc + H2 |
| Clean Architecture (CA) + CQRS delgado | Tests solo entrypoints HTTP; helper no añade dominio/event bus |
| ErrorMessages EN | RF no exige assert de mensajes; asserts por status OK |
| Perfiles local/prod | App intacta; sin perfil `test` dedicado |
| Auth mutaciones / público consultas | A/B con JWT; C sin auth |
| GitHub Issue del RF anotada y **open** hasta close | `#6` OPEN en GitHub y en `rf.md`/`task.md` |
| Commits por tarea (proceso) | Tareas TEST-1…5 marcadas hechas en `task.md` |

## Fuera de alcance (y si apareció hecho)

| Ítem excluido | ¿Hecho de más? |
|---------------|----------------|
| Testcontainers | No |
| `@WithMockUser` como único auth | No |
| Perfil/YAML `test` | No |
| Asserts de body en GET | No |
| Suite amplia / CI / README (006) | No |

Extras permitidos del plan: helper `AuthTestSupport` — presente y alineado.

## Huecos

Ninguno respecto a RF-1 / RF-2 y criterios de aceptación.

## Resumen

La suite mínima del enunciado está cubierta con tres clases `@SpringBootTest` + MockMvc, JWT real vía login seed, solape 409 y listado público 200; Issue #6 open; sin desviaciones de alcance.
