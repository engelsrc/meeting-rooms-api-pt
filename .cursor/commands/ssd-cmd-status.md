---
description: Dice la fase actual y el siguiente paso de un RF
---

# ssd-cmd-status

Dice en pocas líneas dónde está un RF. No modifiques ningún archivo. No hagas commit ni push.

## Antes de responder

1. Lee `.cursor/agents/constitution.md`.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee en `spec/<carpeta>/` los que existan: `rf.md`, `plan.md`, `task.md`, `review-*.md`, `close.md`.

## Respuesta

1. Fase del flujo y estado del RF.
2. Tareas hechas y pendientes (x de y). Si no hay `task.md`, dilo.
3. Si el reloj de la prueba ha arrancado o no (según lo que diga el humano / constitución).
4. Próxima acción, una sola línea:

- sin `rf.md`: `Próxima acción: CREAR EL RF, ejecutar /ssd-cmd-create-rf`;
- con preguntas abiertas o datos pendientes: `Próxima acción: REVISAR EL RF, ejecutar /ssd-cmd-clarify spec/<carpeta>/rf.md`;
- `Estado: borrador` y sin preguntas abiertas: `Próxima acción: APROBAR EL RF. Cambiar la línea a Estado: aprobada en spec/<carpeta>/rf.md.`;
- `Estado: aprobada` y `plan.md`/`task.md` obsoletos o sin `plan.md`: `Próxima acción: CREAR/RECREAR EL PLAN, ejecutar /ssd-cmd-plan spec/<carpeta>/rf.md`;
- con plan vigente y sin `task.md` (o `task.md` obsoleto): `Próxima acción: CREAR LAS TAREAS, ejecutar /ssd-cmd-task spec/<carpeta>/rf.md`;
- con tareas pendientes (y reloj arrancado): `Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/<carpeta>/rf.md`;
- con tareas pendientes y reloj parado: `Próxima acción: ESPERAR ARRANQUE DEL RELOJ (humano) antes de /ssd-cmd-implement`;
- tareas hechas y sin review que cumpla: `Próxima acción: REVISAR EL RESULTADO, ejecutar /ssd-cmd-reviewer spec/<carpeta>/rf.md`;
- review cumple y sin `close.md`: `Próxima acción: INTEGRAR Y CERRAR, ejecutar /ssd-cmd-close spec/<carpeta>/rf.md`;
- con `close.md`: no hay acción siguiente.

No inventes otro comando ni completes el RF.
