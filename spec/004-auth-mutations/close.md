# Close — 004 auth-mutations

**Fecha:** 2026-11-26  
**Review:** `review-001.md` — veredicto **cumple**  
**GitHub Issue:** #5 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/5 — **closed**

## Integración

| Paso | Evidencia |
|------|-----------|
| Rama del RF | `feature/004-auth-mutations` |
| Merge a `dev` local | Fast-forward → `5ee05d1` |
| `git push origin dev` | OK (`origin/dev` @ `5ee05d1`) |
| `git pull origin dev` | Already up to date |
| Verificación | `AuthController`, `V4__create_users.sql`, `SecurityConfig` / JWT en `dev` |
| Issue GitHub | `#5` cerrada tras merge |

## Resultado

RF auth cerrado: `POST /api/auth/login` → JWT; mutaciones con Bearer (403/401); GET públicos; username en `created_by` / `responsible` / `cancelled_by`; users seed admin/user.  
Siguiente típico: `005-minimum-tests` → `006-delivery-readme`.  
Repo: https://github.com/engelsrc/meeting-rooms-api-pt
