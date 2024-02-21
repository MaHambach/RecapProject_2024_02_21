package org.github.mahambach.recapproject_2024_02_21.repositories;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuperKanbanRepo {
    private final List<SuperKanbanToDo> toDoList;

    public List<SuperKanbanToDo> getAllToDos() {
        return toDoList;
    }

    public SuperKanbanToDo getToDoById(String id) {
        for (SuperKanbanToDo superKanbanToDo : toDoList) {
            if (superKanbanToDo.getId().equals(id)) {
                return superKanbanToDo;
            }
        }
        throw new NoSuchToDoFound(id);
    }

    public SuperKanbanToDo createToDo(SuperKanbanToDo toDo) {
        this.toDoList.add(toDo);
        return toDo;
    }

    public SuperKanbanToDo updateToDo(String id, SuperKanbanToDo toDo) {
        if(!id.equals(toDo.getId())){
            throw new IllegalArgumentException("Id in path '" + id + "' and id in body '" +toDo.getId() +"' do not match!");
        }
        for (SuperKanbanToDo superKanbanToDo : toDoList) {
            if (superKanbanToDo.getId().equals(id)) {
                superKanbanToDo.setDescription(toDo.getDescription());
                superKanbanToDo.setStatus(toDo.getStatus());
                return superKanbanToDo;
            }
        }
        throw new NoSuchToDoFound(id);
    }

    public SuperKanbanToDo deleteToDo(String id) {
        for(SuperKanbanToDo superKanbanToDo : toDoList){
            if(superKanbanToDo.getId().equals(id)){
                toDoList.remove(superKanbanToDo);
                return superKanbanToDo;
            }
        }
        throw new NoSuchToDoFound(id);
    }
}
