package org.github.mahambach.recapproject_2024_02_21.controller;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.service.SuperKanbanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SuperKanbanController {

    private final SuperKanbanService superKanbanService;
    @GetMapping("/todo")
    public String getAllToDos(){
        return superKanbanService.getAllToDos();
    }

    @PostMapping("/todo")
    public String createToDo(){
        return superKanbanService.createToDo();
    }

}
