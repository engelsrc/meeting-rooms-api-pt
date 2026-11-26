# Review 001 — 008 extra-postman-collection

**Veredicto:** cumple  
**Fecha:** 2026-11-26  
**RF:** `spec/008-extra-postman-collection/rf.md` (`Estado: aprobada`)  
**Issue:** #9 — open  

## Resumen

Collection Postman v2.1 en ruta fija, 6×(positivo+negativo), scripts JWT/IDs, README extras y smoke con códigos esperados. Sin cambios de código de producto.

## RF por requisito

| RF | Prueba del plan / task | Resultado |
|----|------------------------|-----------|
| RF-1 | DOCS-1, DOCS-2, DOCS-3 + archivo JSON | **cumple** |
| RF-2 | DOCS-2 + payloads en JSON + smoke feliz | **cumple** |
| RF-3 | DOCS-4 + `README.md` sección Extras | **cumple** |
| RF-4 | DOCS-5 notas de status codes | **cumple** |

## Criterios de aceptación

### RF-1
| Criterio | Evidencia |
|----------|-----------|
| Existe `docs/postman/Meeting-Rooms-API.postman_collection.json` v2.1 | Archivo presente; schema Collection v2.1.0 |
| 6 endpoints método/path | Auth/Rooms/Reservations con rutas `/api/auth/login`, `/api/rooms`, `/api/reservations`, `/cancel` |
| ≥1 + y ≥1 − por endpoint | Login OK + bad; Create room + no auth; List rooms + bad page; Create res + overlap; List res + bad date; Cancel + missing |
| `baseUrl` localhost:8080 | variable collection |
| `token`, `roomId`, `reservationId`; sin Environment | variables en JSON; no hay `.postman_environment.json` |
| Login seed + script → `token` | body admin/admin; script `accessToken` |
| Mutaciones `Authorization: Bearer {{token}}` | Create room/reservation/cancel |
| Creates scripts → ids | scripts en Create room / Create reservation |

### RF-2
| Criterio | Evidencia |
|----------|-----------|
| Create room fields | name, capacity, location |
| List rooms page/size | `page=0&size=20` |
| Create reservation | `roomId` var, OffsetDateTime start/end |
| List reservations filters | roomId, date, page, size |
| Cancel body | `reservationId` var |
| Sin rutas inventadas | solo 6 endpoints existentes |

### RF-3
| Criterio | Evidencia |
|----------|-----------|
| Extras / tiempo restante | `README.md` → «### Extras (tiempo restante de la prueba)» |
| Doc API | bullet Documentación de la API (Swagger UI) |
| Postman + ruta | `docs/postman/Meeting-Rooms-API.postman_collection.json` |
| Cambio mínimo | sección corta; secciones §7 de 006 intactas |

### RF-4
| Criterio | Evidencia |
|----------|-----------|
| Smoke feliz 2xx | `task.md` DOCS-5: P1 200, P2 201, P3 200, P4 201, P5 200, P6 200 |
| Smoke todos negativos | N4 409, N1 401, N2 403, N3 400, N5 400, N6 404 |
| Evidencia registrada | notas DOCS-5 en `task.md` (equivalente curl/PowerShell; sin Newman) |

## Constitución

| Aspecto | ¿OK? | Nota |
|---------|------|------|
| CA + CQRS delgado | sí | sin capas nuevas de aplicación |
| ErrorMessages EN | sí | collection no redefine mensajes |
| Perfiles local/prod | N/A | collection agnóstica |
| Auth mutaciones / GET públicos | sí | reflejado en requests |
| Tests ≥3 | N/A | RF Docs; no exige JUnit |
| Issue #N open | sí | #9 OPEN |
| Extra DX / sin secretos reales | sí | seed demo |

## Fuera de alcance (y no hecho de más)

- OpenAPI/Swagger (pertenece a 007; solo referenciado en README) — OK  
- Environment Postman, Newman/CI, monitores — no presentes — OK  
- Código de producto / register/refresh/Basic — no tocado — OK  

## Huecos

- Ninguno material para el veredicto. Smoke documentado vía equivalente HTTP (no ejecución UI Postman); el RF/plan admiten evidencia de status codes sin Newman.

## Veredicto final

**cumple**
