---
description: >-
  Tras review OK: merge a dev local, push origin/dev, pull, verificar, luego
  close.md. No abre PR.
---

# ssd-cmd-close

Cierra el **RF completo** (carpeta) si el revisor acepta. No modifiques el `rf.md`, el plan, las tareas ni el informe de review. **No** abras PR (manuales).

Política Git: **una rama por RF**; commits por tarea en esa rama. La integración a `dev` ocurre **aquí**, al final del RF.

## Antes de cerrar

1. Lee `.cursor/agents/constitution.md` y `.cursor/rules/git-workflow.mdc`.
2. El comando debe incluir la carpeta del RF. Si falta, detente.
3. Lee `spec/<carpeta>/rf.md`. Si no existe, detente.
4. Lee el `review-NNN.md` de número más alto. Si no hay ninguno, detente.
5. Sigue solo si ese veredicto es **cumple**. Si no, detente y lista huecos.
6. Si `dev` no existe, detente.
7. Si ya existe `close.md` en esa carpeta, detente y dilo. No lo sobrescribas.

## Cierre (orden fijo)

1. Asegura commits finales en la **rama del RF** (`feature/<carpeta>` o equivalente; mensajes en **español**). **No** fusiones a `main`.
2. En **`dev` local**: fusiona **esa** rama del RF.
3. Si un conflicto cambia comportamiento, detente y déjalo visible.
4. **`git push origin dev`**.
5. **`git pull origin dev`** (local alineado con remoto).
6. **Verificar** que el desarrollo del RF está en `dev` (p. ej. `git log` / archivos clave / tests rápidos si aplica). Si no, detente y dilo; **no** crees `close.md`.
7. Si no hay `origin`, documenta push pendiente y no fingas sync remoto.
8. **No** es obligatorio cerrar GitHub Issues (Lote 0 local).
9. Solo entonces crea `spec/<carpeta>/close.md` (español): cierre, número del review, veredicto cumple, ramas/commits, evidencia de sync `dev`. Resume en el chat.

## Al terminar

Si el cierre quedó hecho, no hay acción siguiente obligatoria (o la siguiente carpeta `spec/00N-…`). Si te detuviste, di el motivo y no inventes un comando.
