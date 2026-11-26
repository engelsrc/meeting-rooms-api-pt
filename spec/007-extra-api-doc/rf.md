# RF — Documentación OpenAPI / Swagger (extra)

Estado: aprobada

## Metadatos
- Carpeta: `spec/007-extra-api-doc/`
- Slug: `extra-api-doc`
- Orden: `007`
- Tipo: `Docs` | `API`
- Origen: change — AskQuestions tras clarify (perfiles, URL UI, errores mínimos, README, Bearer, assets públicos)
- Fecha: 2026-11-26
- GitHub Issue: #8 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/8

## Problema / oportunidad

La API de salas ya cumple lo obligatorio del enunciado, pero un revisor no tiene una UI interactiva ni un contrato OpenAPI 3 para explorar endpoints, auth JWT y códigos de error principales sin leer controllers a mano. La constitución admite **documentación de API** como extra útil si sobra tiempo.

## Objetivo

Exponer documentación OpenAPI 3 + Swagger UI **delgada** (springdoc) sobre la API existente, disponible en perfiles **`local` y `prod`**, sin romper auth JWT ni Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS).

## Alcance

### Incluye
- Dependencia Maven compatible con Spring Boot **3.3.x** (p. ej. `springdoc-openapi-starter-webmvc-ui` en línea 2.6.x).
- Publicación de la especificación OpenAPI en `/v3/api-docs` y de **Swagger UI** en la URL canónica `http://localhost:8080/swagger-ui/index.html`.
- Acceso **sin autenticación** a rutas de documentación: UI, `/v3/api-docs` y assets estáticos necesarios (`/swagger-ui/**` y equivalentes de springdoc).
- Docs habilitadas en **todos** los perfiles de la app (`local` y `prod`).
- Esquema de seguridad **HTTP Bearer (JWT)** en OpenAPI (botón «Authorize» / «Try it out»); **sin** documentar en el RF el flujo login → pegar token.
- Descubrimiento de los endpoints REST ya existentes (salas, reservas, login).
- Documentación **mínima** en OpenAPI de códigos de respuesta principales: **400**, **401/403**, **409** (sin ejemplos elaborados por código).
- Actualizar el **README ya existente** con la URL canónica de Swagger UI (solo esa; **sin** exigir `/v3/api-docs` en el README).

### Excluye
- OpenAPI «enterprise»: anotaciones densas, ejemplos elaborados por código de estado, codegen de clientes, export CI del `openapi.json`.
- Flujo OAuth2/password u otro automatismo en la UI para obtener el JWT (solo esquema Bearer).
- Mencionar `/v3/api-docs` en el README (fuera de alcance documental del README).
- Nuevos endpoints de negocio o cambios de contrato HTTP (rutas, códigos, cuerpos de negocio).
- Cambiar el modelo de auth de la API (sigue JWT Bearer).
- Docker Compose, métricas u otros extras no pedidos en este RF.
- Springfox u otras libs fuera de springdoc alineado a Boot 3.
- Deshabilitar Swagger solo en `prod`.

## Actores
- Humano: aprueba RF; usa Swagger UI para revisar/entregar.
- Agente: añade dependencia y configuración mínima; anota códigos de error mínimos; actualiza README existente.
- Revisor externo: abre Swagger UI; usa Bearer JWT según el esquema de la UI.

## Requisitos funcionales

### RF-1 — Dependencia y arranque de documentación
**CUANDO** la aplicación arranca con perfil `local` o `prod`, **EL SISTEMA** sirve OpenAPI 3 y Swagger UI sin pasos de build extra.

#### Criterios de aceptación
- [ ] El `pom.xml` incluye `springdoc-openapi-starter-webmvc-ui` con versión compatible con Spring Boot 3.3.5.
- [ ] Tras arrancar (perfil `local` o `prod`), Swagger UI responde en `http://localhost:8080/swagger-ui/index.html`.
- [ ] La especificación está disponible en `http://localhost:8080/v3/api-docs`.

### RF-2 — Documentación pública; mutaciones siguen con JWT
**CUANDO** un cliente abre la UI, sus assets o el JSON de OpenAPI, **EL SISTEMA** no exige token para esas rutas; las mutaciones de `/api/**` siguen exigiendo JWT como hoy.

#### Criterios de aceptación
- [ ] Rutas públicas sin `Authorization`: `/swagger-ui/index.html`, `/swagger-ui/**` (assets), `/v3/api-docs` (y redirecciones equivalentes de springdoc si aplican).
- [ ] POST/DELETE (u otras mutaciones) de negocio bajo `/api/**` siguen autenticadas con Bearer JWT.
- [ ] La UI declara esquema **Bearer JWT** usable desde «Authorize» / «Try it out».
- [ ] El RF **no** exige documentar ni automatizar el flujo login → copiar token.

### RF-3 — Cobertura mínima de la API existente
**CUANDO** se abre Swagger UI, **EL DOCUMENTO** lista los endpoints de producto ya implementados (salas, reservas, auth login).

#### Criterios de aceptación
- [ ] Aparecen al menos: login, crear/listar salas, crear/listar/cancelar reservas (métodos y rutas coherentes con la API en `dev`).
- [ ] No se inventan operaciones que no existan en código.
- [ ] Anotaciones OpenAPI opcionales y **mínimas** (tags / descripción breve); no bloquean el RF si el auto-descubrimiento basta.

### RF-4 — README apunta a la UI
**CUANDO** un revisor lee el README raíz (ya existente tras `006`), **EL DOCUMENTO** indica la URL canónica de Swagger UI.

#### Criterios de aceptación
- [ ] El README menciona `http://localhost:8080/swagger-ui/index.html`.
- [ ] El README **no** está obligado a mencionar `/v3/api-docs`.
- [ ] No sustituye ni reescribe las secciones obligatorias del enunciado §7 ya cubiertas en `006`.

### RF-5 — Códigos de error mínimos en OpenAPI
**CUANDO** se consulta la especificación OpenAPI / Swagger UI, **EL DOCUMENTO** declara de forma mínima los códigos de error principales de la API.

#### Criterios de aceptación
- [ ] Quedan documentados al menos: **400**, **401** y/o **403**, **409** (según aplique a cada operación o de forma global/reutilizable).
- [ ] Sin ejemplos elaborados de cuerpo por cada código de estado.
- [ ] No cambia el comportamiento HTTP real de la API.

## Reglas de negocio
- Extra: no es requisito del enunciado; prioridad inferior a lo ya entregado en `001`–`006`; permitido por la constitución si sobra tiempo.
- Documentación **delgada**: valor al revisor sin sobreingeniería en la ventana de 3 h.
- Auth de negocio sin cambios de semántica (GET públicos; mutaciones JWT).
- Docs activas en **todos** los perfiles (`local` y `prod`).

## Casos límite
- Si Security o el filtro JWT bloquean UI, `/swagger-ui/**` o `/v3/api-docs`: ajustar solo lo necesario para permitir docs; no debilitar mutaciones `/api/**`.
- Token inválido en «Try it out»: mismo comportamiento HTTP de la API (401/403), sin lógica nueva en OpenAPI.
- README ausente: no aplica (README ya existe); este RF solo añade la mención de Swagger UI.

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; la config OpenAPI vive en infraestructura/entrada, sin filtrar dominio.
- Mensajes de API en inglés vía `ErrorMessages` (sin literales nuevos de error por este RF).
- Perfiles Spring `local` / `prod`.
- Sin event bus / infra de más en la ventana de 3 h.
- Chat/SDD en español; código y mensajes de API en inglés.
- Una GitHub Issue **open** antes de `/ssd-cmd-implement`; cierre solo en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions del change.

## Impacto en plan/task (aún no editar)
- Aún no existen `plan.md` / `task.md`: crear tras APROBADO con `/ssd-cmd-plan` y `/ssd-cmd-task`.
- El plan deberá cubrir: (1) dependencia springdoc, (2) seguridad rutas docs + Bearer, (3) códigos 400/401|403/409 mínimos, (4) README con URL UI canónica.

## Siguiente paso
`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/007-extra-api-doc/rf.md`
