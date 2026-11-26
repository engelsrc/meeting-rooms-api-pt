---
description: Revisa el RF requisito por requisito, sin corregir
---

# ssd-cmd-reviewer

Revisa el resultado requisito por requisito. No corrijas nada. No modifiques código, el `rf.md`, el plan ni las tareas. No ejecutes Git.

## Antes de escribir el resultado

1. Lee `.cursor/agents/constitution.md`. Prevalece el enunciado vigente.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md`. Si no existe, detente.
4. Lee `plan.md` y `task.md` en esa carpeta. Si no hay plan, los RF quedan sin prueba asociada.
5. Recorre cada `RF`. Indica qué prueba del plan lo cubre y el resultado. Sin evidencia = no cumple.
6. Contrasta cada criterio de aceptación con evidencia. Sin evidencia = no cumple.
7. Contrasta con la constitución: CA+CQRS delgado, `ErrorMessages` EN, perfiles si aplica, auth en mutaciones / público en consultas, ≥3 tests con ≥1 integración cuando el alcance lo exija.
8. Señala lo que el RF deja fuera de alcance y aun así aparezca hecho.
9. **No** exijas revisión de UI/navegador salvo que el RF sea de tipo UI (esta prueba es API REST).

## Archivo

En la carpeta del RF, busca `review-NNN.md` y crea el siguiente número (`review-001.md` si no hay ninguno). No sobrescribas un resultado anterior.

Informe breve (español): veredicto (cumple | no cumple), cada `RF` con prueba/resultado, cada criterio con evidencia o hueco, contraste con constitución, fuera de alcance y huecos. Resume el veredicto en el chat.

## Al terminar

Si no cumple:

`Próxima acción: PEDIR EL CAMBIO, ejecutar /ssd-cmd-change spec/<carpeta>/rf.md`

(Si el fallo es solo de código/tests sin cambio de requisito, dilo y sugiere volver a `/ssd-cmd-implement` en las tareas abiertas.)

Si cumple:

`Próxima acción: INTEGRAR Y CERRAR — ejecutar /ssd-cmd-close spec/<carpeta>/rf.md` (merge `dev` local → push → pull → verificar → `close.md`)
