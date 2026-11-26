# RF — Actualizar README: uso de IA (organización)

Estado: aprobada

## Metadatos
- Carpeta: `spec/009-readme-ai-usage/`
- Slug: `readme-ai-usage`
- Orden: `009`
- Tipo: `Docs`
- Origen: humano — agregar bullet **Organicé** en «Cómo usé la IA»
- Fecha: 2026-11-26
- GitHub Issue: #10 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/10

## Problema / oportunidad

El README ya documenta cómo se usó la IA (**Pedí** / **Acepté** / **Corregí**), pero no refleja que se organizó el entorno de trabajo con comandos Cursor, skills y constitución del proyecto para agilizar el desarrollo y acotar el uso de la IA.

## Objetivo

Actualizar la sección **Cómo usé la IA** del `README.md` para incluir un hecho **Organicé** que describa esa preparación, sin cambiar el resto del README salvo lo estrictamente necesario para coherencia del bloque.

## Alcance

### Incluye
- Edición de `README.md` (raíz): sección «Cómo usé la IA».
- Bullet **Organicé** con el sentido acordado (comandos, skills, constitución).
- Orden del bloque: Organicé → Pedí → Acepté → Corregí.

### Excluye
- Cambiar endpoints, stack, decisiones técnicas u otras secciones del README.
- Crear o modificar comandos, skills o constitución (solo documentarlos).
- Cambiar código de la API o tests.
- Inventar hechos distintos a los aportados por el autor.

## Actores
- Humano: aporta el hecho **Organicé** y aprueba el RF.
- Agente: formatea e inserta el bullet en el README.
- Revisor externo: lee el apartado IA como evidencia del proceso.

## Requisitos funcionales

### RF-1 — Bullet Organicé
WHEN se lea la sección «Cómo usé la IA» del README, THE sistema SHALL incluir un bullet **Organicé** que mencione comandos Cursor (`ssd-cmd-*`), skills/reglas y la constitución del proyecto como marco para agilizar el desarrollo y dirigir a la IA.

### RF-2 — Conservar bullets existentes
WHEN se actualice ese bloque, THE sistema SHALL conservar los bullets existentes **Pedí**, **Acepté** y **Corregí** (contenido actual, sin reescribirlos salvo tipografía/orden).

### RF-3 — Orden del bloque
WHEN se ordene el bloque, THE sistema SHALL poner **Organicé** antes de **Pedí** / **Acepté** / **Corregí**.

## Fuera de alcance

- Reescritura general del README.
- Nuevos extras de producto (Postman, Swagger, etc.).
- Ajustes de tiempo/`PRUEBA-INICIO.md` salvo que el humano lo pida aparte.

## Criterios de aceptación

- [ ] `README.md` incluye en «Cómo usé la IA» el bullet **Organicé** con el sentido acordado.
- [ ] El bloque queda en orden: Organicé → Pedí → Acepté → Corregí.
- [ ] No se inventan hechos distintos a los aportados por el autor.
- [ ] El resto del README permanece intacto.

## Texto orientativo (redacción sugerida)

- **Organicé:** comandos Cursor (`ssd-cmd-*`), skills y constitución del proyecto para acotar el trabajo de la IA, acelerar el flujo SDD y mantener criterios consistentes.

## Preguntas abiertas

- Ninguna bloqueante: el alcance es solo documentar el hecho **Organicé** en el README.

## Restricciones heredadas

- Documentación SDD y chat en español; no inventar requisitos de producto.
- Sin cambios de arquitectura ni de API en este RF.
- Una rama por RF / una Issue por RF según flujo del repo (al planificar e implementar).
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado no aplican a cambios de código aquí (RF solo Docs).
