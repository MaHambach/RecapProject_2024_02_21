package org.github.mahambach.recapproject_2024_02_21.repositories;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.OperationEvent;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.github.mahambach.recapproject_2024_02_21.service.CareTakerService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuperKanbanRepo {
    private final CareTakerService careTakerService;
    
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
        careTakerService.add(toDo.saveStateMemento(OperationEvent.CREATE));
        return toDo;
    }

    public SuperKanbanToDo updateToDo(String id, SuperKanbanToDo toDo) {
        if(!id.equals(toDo.getId())){
            throw new IllegalArgumentException("Id in path '" + id + "' and id in body '" + toDo.getId() +"' do not match!");
        }
        for (SuperKanbanToDo superKanbanToDo : toDoList) {
            if (superKanbanToDo.getId().equals(id)) {
                careTakerService.add(toDo.saveStateMemento(OperationEvent.UPDATE));
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
                careTakerService.add(superKanbanToDo.saveStateMemento(OperationEvent.DELETE));
                return superKanbanToDo;
            }
        }
        throw new NoSuchToDoFound(id);
    }

    public SuperKanbanToDo undo() {
        SuperKanbanToDoMemento memento = careTakerService.undo();
        SuperKanbanToDo superKanbanToDo = new SuperKanbanToDo();
        superKanbanToDo.restoreStateMemento(memento);

        switch (memento.operationEvent()) {
            case CREATE:
                toDoList.remove(superKanbanToDo);
                break;
            case UPDATE:
                for (SuperKanbanToDo toDo : toDoList) {
                    if (toDo.getId().equals(superKanbanToDo.getId())) {
                        toDoList.remove(toDo);
                        toDoList.add(superKanbanToDo);
                        break;
                    }
                }
                break;
            case DELETE:
                toDoList.add(superKanbanToDo);
                break;
        }
        return superKanbanToDo;
    }

    public SuperKanbanToDo redo() {
        SuperKanbanToDoMemento memento = careTakerService.redo();
        SuperKanbanToDo superKanbanToDo = new SuperKanbanToDo();
        superKanbanToDo.restoreStateMemento(memento);

        switch (memento.operationEvent()) {
            case CREATE:
                toDoList.add(superKanbanToDo);
                break;
            case UPDATE:
                for (SuperKanbanToDo toDo : toDoList) {
                    if (toDo.getId().equals(superKanbanToDo.getId())) {
                        toDoList.remove(toDo);
                        toDoList.add(superKanbanToDo);
                        break;
                    }
                }
                break;
            case DELETE:
                toDoList.remove(superKanbanToDo);
                break;
        }

        return superKanbanToDo;
    }
}
