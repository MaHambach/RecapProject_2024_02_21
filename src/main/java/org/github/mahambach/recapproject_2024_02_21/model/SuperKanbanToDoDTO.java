package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class SuperKanbanToDoDTO {
    private String description;
    private String status;
}
