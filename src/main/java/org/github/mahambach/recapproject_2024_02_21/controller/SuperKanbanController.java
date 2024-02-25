package org.github.mahambach.recapproject_2024_02_21.controller;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.github.mahambach.recapproject_2024_02_21.service.SuperKanbanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SuperKanbanController {

    private final SuperKanbanService superKanbanService;
    @GetMapping("/todo")
    public List<SuperKanbanToDo> getAllToDos(){
         return superKanbanService.getAllToDos();
    }

    @GetMapping({"/todo/{id}"})
    public SuperKanbanToDo getToDoById(@PathVariable String id){
        return superKanbanService.getToDoById(id);
    }

    @PostMapping("/todo")
    @ResponseStatus(HttpStatus.CREATED)
        public SuperKanbanToDo createToDo(@RequestBody SuperKanbanToDoDTO toDoDTO){
        return superKanbanService.createToDo(toDoDTO);
    }

    @PutMapping("/todo/{id}")
    public SuperKanbanToDo updateToDo(@PathVariable String id, @RequestBody SuperKanbanToDo toDo){
        return superKanbanService.updateToDo(id, toDo);
    }

    @DeleteMapping("/todo/{id}")
    public SuperKanbanToDo deleteToDo(@PathVariable String id){
        return superKanbanService.deleteToDo(id);
    }

    @PostMapping("/undo")
    public SuperKanbanToDoMemento undo(){
        return superKanbanService.undo();
    }

//    @PostMapping("/redo")
//    public SuperKanbanToDoMemento redo(){
//        return superKanbanService.redo();
//    }
}
