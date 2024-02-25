package org.github.mahambach.recapproject_2024_02_21.service;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
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

    public List<SuperKanbanToDo> getAllToDos() {
        return this.superKanbanRepo.findAll();
    }

    public SuperKanbanToDo getToDoById(String id) {
        return this.superKanbanRepo.findById(id).orElseThrow(() -> new NoSuchToDoFound(id));
    }

    public SuperKanbanToDo createToDo(SuperKanbanToDoDTO toDoDTO) {
        //return this.superKanbanRepo.createToDo(new SuperKanbanToDo(idService.generateId(), chatGptService.spellCheck(toDoDTO.getDescription()), toDoDTO.getStatus()));  // Entfernt, da es in Zukunft, wenn der Key nicht länger funktioniert, probleme geben könnte.
        return this.superKanbanRepo.save(new SuperKanbanToDo(idService.generateId(), toDoDTO.getDescription(), toDoDTO.getStatus()));
    }

    public SuperKanbanToDo updateToDo(String id, SuperKanbanToDo toDo) {
        if(!id.equals(toDo.getId())) {
            throw new IllegalArgumentException("The id in the path and the id in the body do not match.");
        }
        return this.superKanbanRepo.save(toDo); //Bewusste Entscheidung gegen einen Rechtschreib- und Grammatik-Check durch ChatGPT um den Benutzer die Möglichkeit zu geben Fehler von ChatGPT zu korrigieren.
    }

    public SuperKanbanToDo deleteToDo(String id) {
        SuperKanbanToDo toDo = this.superKanbanRepo.findById(id).orElseThrow(() -> new NoSuchToDoFound(id));
        this.superKanbanRepo.deleteById(id);
        return toDo;
    }


    public SuperKanbanToDoMemento undo() {
        return this.superKanbanRepo.undo();
    }

    public SuperKanbanToDoMemento redo() {
        return this.superKanbanRepo.redo();
    }
}
