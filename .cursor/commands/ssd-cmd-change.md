---
description: Cambia un requisito en el RF y espera aprobación
---

# ssd-cmd-change

Incorpora un requisito nuevo o un cambio en el RF. No toques código. No hagas commit ni push. El RF dice el qué y el porqué. No inventes stack fuera del enunciado / constitución.

## Antes de editar

1. Lee `.cursor/agents/constitution.md`. Si el cambio la contradice, detente y dilo; no edites.
2. El comando debe incluir la carpeta del RF y el requisito nuevo o el cambio. Si falta alguno, pregunta y espera. No edites.
3. Lee `spec/<carpeta>/rf.md`. Si no existe, detente.

## Spec (antes de APROBADO)

Actualiza solo `rf.md` (español). El requisito nuevo recibe el siguiente `RF-n`. Si cambias uno existente, conserva su identificador. Redáctalo de forma verificable. Actualiza casos límite y fuera de alcance. Deja la línea exactamente: `Estado: borrador`. No inventes reglas de negocio.

Indica qué partes de `plan.md` y `task.md` habría que cambiar después. **Aún no las edites** (salvo el marcado de obsoleto **después** de APROBADO).

Muestra el diff de `rf.md` y espera la aprobación. Sin esa aprobación no sigas.

## Tras la respuesta APROBADO (obligatorio)

Cuando el humano responda **APROBADO**:

1. En `rf.md`, cambia la línea a exactamente: `Estado: aprobada`.
2. Si existen `plan.md` y/o `task.md` en esa carpeta:
   - Márcalos **obsoletos**: al inicio de cada uno, bajo el título, añade exactamente:

     `Estado: obsoleto — regenerar tras cambio del RF`

   - **No** regeneres su contenido en este comando.
3. Si no existen `plan.md` ni `task.md`, no crees ninguno aquí.

## Al terminar

**Antes** de APROBADO:

`Próxima acción: APROBAR CAMBIOS. Responder APROBADO sobre spec/<carpeta>/rf.md.`

**Después** de APROBADO:

- Si `plan.md` o `task.md` quedaron obsoletos (o no hay `plan.md`):

  `Próxima acción: RECREAR EL PLAN, ejecutar /ssd-cmd-plan spec/<carpeta>/rf.md`

- Si aún no había plan ni tasks y el RF ya está `aprobada` (mismo caso práctico):

  `Próxima acción: CREAR EL PLAN, ejecutar /ssd-cmd-plan spec/<carpeta>/rf.md`
