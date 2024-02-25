package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.With;

@With
public record SuperKanbanToDoMemento (OperationEvent operationEvent,
                                      String id,
                                      String description,
                                      String status){
}
