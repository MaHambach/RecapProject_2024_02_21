package org.github.mahambach.recapproject_2024_02_21.service;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.OperationEvent;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.github.mahambach.recapproject_2024_02_21.repositories.SuperKanbanRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperKanbanService {
    private final SuperKanbanRepo superKanbanRepo;
    //private final ChatGptService chatGptService; // Entfernt, da es in Zukunft, wenn der Key nicht länger funktioniert, probleme geben könnte.
    private final IdService idService;
    private final CareTakerService careTakerService;

    public List<SuperKanbanToDo> getAllToDos() {

        return this.superKanbanRepo.findAll();
    }

    public SuperKanbanToDo getToDoById(String id) {
        return this.superKanbanRepo.findById(id).orElseThrow(() -> new NoSuchToDoFound(id));
    }

    public SuperKanbanToDo createToDo(SuperKanbanToDoDTO toDoDTO) {
        //return this.superKanbanRepo.createToDo(new SuperKanbanToDo(idService.generateId(), chatGptService.spellCheck(toDoDTO.getDescription()), toDoDTO.getStatus()));  // Entfernt, da es in Zukunft, wenn der Key nicht länger funktioniert, probleme geben könnte.
        SuperKanbanToDo toDo = new SuperKanbanToDo(idService.generateId(), toDoDTO.getDescription(), toDoDTO.getStatus());
        this.careTakerService.add(toDo.saveStateMemento(OperationEvent.CREATE));
        return this.superKanbanRepo.save(toDo);
    }

    public SuperKanbanToDo updateToDo(String id, SuperKanbanToDo toDo) {
        SuperKanbanToDo oldToDo = this.superKanbanRepo.findById(id).orElseThrow(() -> new NoSuchToDoFound(id));
        if(!id.equals(toDo.getId())) {
            throw new IllegalArgumentException("The id in the path and the id in the body do not match.");
        }
        this.careTakerService.add(oldToDo.saveStateMemento(OperationEvent.UPDATE));
        this.careTakerService.add(toDo.saveStateMemento(OperationEvent.UPDATE));
        return this.superKanbanRepo.save(toDo); //Bewusste Entscheidung gegen einen Rechtschreib- und Grammatik-Check durch ChatGPT um den Benutzer die Möglichkeit zu geben Fehler von ChatGPT zu korrigieren.
    }

    public SuperKanbanToDo deleteToDo(String id) {
        SuperKanbanToDo toDo = this.superKanbanRepo.findById(id).orElseThrow(() -> new NoSuchToDoFound(id));
        this.careTakerService.add(toDo.saveStateMemento(OperationEvent.DELETE));
        this.superKanbanRepo.deleteById(id);
        return toDo;
    }


    public SuperKanbanToDoMemento undo() {
        SuperKanbanToDoMemento memento = careTakerService.undo();
        if(memento.operationEvent().equals(OperationEvent.UPDATE)) memento = careTakerService.undo();

        SuperKanbanToDo superKanbanToDo = new SuperKanbanToDo();
        superKanbanToDo.restoreStateMemento(memento);

        switch (memento.operationEvent()) {
            case CREATE:
                deleteToDo(superKanbanToDo.getId());
                break;
            case UPDATE:
                updateToDo(superKanbanToDo.getId(), superKanbanToDo);
                break;
            case DELETE:
                this.superKanbanRepo.save(superKanbanToDo); // Wir müssen hier die neue creatToDo-Methode umgehen, da diese dem superKanbanToDo eine neue ID zuweisen würde.
                this.careTakerService.add(superKanbanToDo.saveStateMemento(OperationEvent.CREATE));

                break;
        }
        return memento;
    }

    public SuperKanbanToDoMemento redo() {
        SuperKanbanToDoMemento memento = careTakerService.redo();
        if(memento.operationEvent().equals(OperationEvent.UPDATE)) memento = careTakerService.redo();

        SuperKanbanToDo superKanbanToDo = new SuperKanbanToDo();
        superKanbanToDo.restoreStateMemento(memento);

        switch (memento.operationEvent()) {
            case CREATE:
                this.superKanbanRepo.save(superKanbanToDo); // Wir müssen hier die neue creatToDo-Methode umgehen, da diese dem superKanbanToDo eine neue ID zuweisen würde.
                this.careTakerService.add(superKanbanToDo.saveStateMemento(OperationEvent.CREATE));

                break;
            case UPDATE:
                updateToDo(superKanbanToDo.getId(), superKanbanToDo);
                break;
            case DELETE:
                deleteToDo(superKanbanToDo.getId());
                break;
        }

        return memento;
    }

}
