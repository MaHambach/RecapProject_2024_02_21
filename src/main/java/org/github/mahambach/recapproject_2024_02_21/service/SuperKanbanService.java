package org.github.mahambach.recapproject_2024_02_21.service;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.repositories.SuperKanbanRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperKanbanService {
    private final SuperKanbanRepo superKanbanRepo;
    private final IdService idService;

    public List<SuperKanbanToDo> getAllToDos() {
        return this.superKanbanRepo.getAllToDos();
    }

    public SuperKanbanToDo getToDoById(String id) {
        return this.superKanbanRepo.getToDoById(id);
    }

    public SuperKanbanToDo createToDo(SuperKanbanToDoDTO toDoDTO) {
        return this.superKanbanRepo.createToDo(new SuperKanbanToDo(idService.generateId(), toDoDTO.getDescription(), toDoDTO.getStatus()));
    }

    public SuperKanbanToDo updateToDo(String id, SuperKanbanToDo toDo) {
        return this.superKanbanRepo.updateToDo(id, toDo);
    }

    public SuperKanbanToDo deleteToDo(String id) {
        return this.superKanbanRepo.deleteToDo(id);
    }


}
