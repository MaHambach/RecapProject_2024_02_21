package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class SuperKanbanToDo {
    private String id;
    private String description;
    private String status;

    public SuperKanbanToDoMemento saveStateMemento(OperationEvent operationEvent) {
        return new SuperKanbanToDoMemento(operationEvent, this.id, this.description, this.status);
    }

    public void restoreStateMemento(SuperKanbanToDoMemento memento) {
        this.id = memento.id();
        this.description = memento.description();
        this.status = memento.status();
    }
}
