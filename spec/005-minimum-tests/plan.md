# Plan — 005 minimum-tests

## Metadatos
- Carpeta: `spec/005-minimum-tests/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama: `feature/005-minimum-tests` (desde `dev`)
- Issue: #6 (open)
- Dependencia: RF 004 cerrado

## Enfoque y orden

1. Helper mínimo de test (opcional): login MockMvc → extraer `accessToken` (reutilizable en clases A/B).
2. **Clase A** — create room 201 con JWT (RF-1).
3. **Clase B** — overlap reservation 409 con mismo JWT (RF-1).
4. **Clase C** — GET rooms 200 sin auth (RF-1).
5. Verificar `mvn test` verde (RF-1, RF-2).

Sin perfil `test`; `@SpringBootTest` + `@AutoConfigureMockMvc`; H2 del perfil default (`prod`) o properties inline si el contexto de test lo requiere (p. ej. `spring.datasource.url` H2 mem + JWT secret demo).

## Clean Architecture (CA) + CQRS delgado

Los tests ejercitan **entrypoints HTTP** (MockMvc); no duplican lógica de dominio en el test. No se añaden puertos/event bus. Helpers solo parsean JSON de login.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `src/test/java/.../CreateRoomAuthTest.java` (nombre orientativo) | Login + POST `/api/rooms` → 201 | RF-1 A |
| `src/test/java/.../ReservationOverlapTest.java` | Login + room + 2 creates solapados → 409 | RF-1 B |
| `src/test/java/.../ListRoomsPublicTest.java` | GET `/api/rooms` sin auth → 200 | RF-1 C |
| (opcional) `…/support/AuthTestSupport.java` | `loginAdmin(mockMvc)` → token | RF-1/2 |
| `pom.xml` | Solo si falta dep de test (ya hay starter-test + security-test) | — |

Paquete sugerido: `com.meetingrooms` o `com.meetingrooms.api`.

## Algoritmos (pseudocódigo)

### Helper login

```text
PERFORM POST /api/auth/login { username: "admin", password: "admin" }
ASSERT 200
RETURN json.accessToken
```

### Clase A

```text
token = loginAdmin()
PERFORM POST /api/rooms
  Authorization: Bearer {token}
  body: { name: uniqueName(), capacity: 4 }
ASSERT status 201
```

### Clase B

```text
token = loginAdmin()
roomId = POST /api/rooms (Bearer) → id
POST /api/reservations (Bearer) roomId, start=T, end=T+1h → 201
POST /api/reservations (Bearer) roomId, start=T+30m, end=T+90m → ASSERT 409
```

### Clase C

```text
PERFORM GET /api/rooms   // no Authorization
ASSERT status 200
```

## Mensajes de error

Asserts por **status HTTP** (201/409/200). No obligatorio assert de `ErrorMessages` en este RF.

## Perfiles `local` / `prod`

App sigue con `prod` default H2. Tests: sin `@ActiveProfiles("test")`; si hace falta, properties en `@SpringBootTest` para JWT secret (mismo largo mínimo HS256) y datasource H2 mem.

## Estrategia de pruebas

Esta es la suite mínima del enunciado: **3 clases MockMvc**, todas integración HTTP; solape usa JWT real (filtro).  
Más tests → fuera de alcance / extras posteriores.

## Fuera de alcance

Testcontainers, WithMockUser-only, YAML test, asserts body GET, CI, README.

## Trazabilidad

| Id | Cubierto por |
|----|----------------|
| RF-1 | Tres clases IT + `mvn test` |
| RF-2 | Bearer vía login; MockMvc; sin Testcontainers |

## Próximo paso

`/ssd-cmd-task` → `task.md` + GitHub Issue **open**.
