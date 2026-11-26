---
description: Revisa el RF y solo detecta huecos, sin resolverlos
---

# ssd-cmd-clarify

Revisa un RF. Solo detecta. No propongas soluciones, no modifiques el `rf.md` y no escribas plan, tareas ni código. No ejecutes Git.

## Antes de listar

1. Lee `.cursor/agents/constitution.md`. Si discrepan otras reglas, prevalece el **enunciado vigente** de la constitución.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md`. Si no existe, detente.

## Informe

Responde en el chat con una lista numerada. **No crees archivos.** Cada hallazgo indica de cuál de estos cuatro tipos es:

1. **Ambigüedad:** un requisito que no se puede verificar.
2. **Contradicción** entre requisitos.
3. **Caso límite** no cubierto.
4. **Conflicto** con `.cursor/agents/constitution.md` (enunciado / decisiones de repo: CA+CQRS delgado, `ErrorMessages` EN, perfiles `local`/`prod`, reloj, etc.).

Si un tipo no tiene hallazgos, dilo. No inventes reglas de negocio ni completes el RF.

## Al terminar

Cierra con una sola línea. El comando va seguido del RF.

Si hay huecos que cambian el RF:

`Próxima acción: PEDIR EL CAMBIO, ejecutar /ssd-cmd-change spec/<carpeta>/rf.md`

Si no hay huecos:

`Próxima acción: APROBAR EL RF. Cambiar la línea a Estado: aprobada en spec/<carpeta>/rf.md.`
