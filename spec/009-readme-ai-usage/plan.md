# Plan — 009 readme-ai-usage

## Metadatos
- Carpeta: `spec/009-readme-ai-usage/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama prevista: `feature/009-readme-ai-usage` (desde `dev`)
- Issue: #10 (open) — https://github.com/engelsrc/meeting-rooms-api-pt/issues/10
- Dependencia: RF 006 entregó el README base; este RF solo amplía «Cómo usé la IA»

## Enfoque y orden

1. **Leer** el bloque actual «Cómo usé la IA» en `README.md` (Pedí / Acepté / Corregí).
2. **RF-1 + RF-3** — Insertar el bullet **Organicé** (texto orientativo del RF) **antes** de Pedí.
3. **RF-2** — Verificar que Pedí / Acepté / Corregí quedan con el mismo contenido (solo cambió el orden relativo al nuevo bullet).
4. **Verificación visual** — Resto del README intacto (Cómo ejecutarlo, Endpoints, Decisiones, Tiempo, etc.).

Un solo archivo de entrega: `README.md`. Sin código de producto.

## Clean Architecture (CA) + CQRS delgado

RF **Docs**: no crea ni modifica capas, commands, queries ni adapters. No aplica cambio de paquetes. No se toca `ErrorMessages` ni perfiles Spring.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `README.md` (raíz) | Insertar bullet **Organicé** y reordenar el bloque IA | RF-1, RF-2, RF-3 |

Solo lectura (sin modificar): constitución, comandos `ssd-cmd-*`, rules/skills — se documentan, no se editan.

## Cambio concreto en README

Bloque objetivo tras el cambio:

```text
## Cómo usé la IA

Hechos aportados por el autor de la prueba (formateados; sin ampliar):

- **Organicé:** comandos Cursor (`ssd-cmd-*`), skills y constitución del proyecto para acotar el trabajo de la IA, acelerar el flujo SDD y mantener criterios consistentes.
- **Pedí:** … (contenido actual sin reescribir)
- **Acepté:** … (contenido actual sin reescribir)
- **Corregí:** … (contenido actual sin reescribir)
```

## Algoritmo / reglas (pseudocódigo)

```text
EDITAR README.md sección «Cómo usé la IA»:
  INSERTAR bullet Organicé con texto del RF (texto orientativo)
  COLOCAR Organicé como primer bullet del listado
  CONSERVAR pedí/acepté/corregí textualmente
  NO modificar otras secciones del README
  NO inventar hechos adicionales
```

## Mensajes de error / perfiles

- No aplica: sin cambios de API ni YAML.

## Estrategia de pruebas

- No aplica suite automatizada (RF Docs).
- Verificación manual: leer el bloque IA y confirmar orden + texto; `git diff` acotado a `README.md` en esa sección.

## Fuera de alcance

- Reescritura general del README.
- Extras de producto (Postman, Swagger, etc.).
- Ajustes de sección Tiempo / `PRUEBA-INICIO.md`.
- Código Java, tests, comandos, skills, constitución.
