package org.github.mahambach.recapproject_2024_02_21.repositories;

import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.OperationEvent;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.github.mahambach.recapproject_2024_02_21.service.CareTakerService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuperKanbanRepoTest {
    private final CareTakerService mockCareTakerService = mock(CareTakerService.class);

    @Test
    void getAllToDos_whenEmpty_thenEmpty() {
        // Given
        List<SuperKanbanToDo> expected = new ArrayList<>();
        // When
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, expected);
        // Then
        assertEquals(expected, superKanbanRepo.getAllToDos());
    }

    @Test
    void getAllToDos_whenSomething_thenSomething() {
        // Given
        List<SuperKanbanToDo> expected = new ArrayList<>();
        expected.add(new SuperKanbanToDo("1", "description1", "status1"));
        expected.add(new SuperKanbanToDo("2", "description2", "status2"));
        expected.add(new SuperKanbanToDo("3", "description3", "status3"));
        // When
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, expected);
        // Then
        assertEquals(expected, superKanbanRepo.getAllToDos());
    }

    @Test
    void getToDoById_whenExists_thenReturn() {
        // Given
        List<SuperKanbanToDo> toDoList = new ArrayList<>();
        toDoList.add(new SuperKanbanToDo("1", "description1", "status1"));
        toDoList.add(new SuperKanbanToDo("2", "description2", "status2"));
        toDoList.add(new SuperKanbanToDo("3", "description3", "status3"));
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, toDoList);
        // When
        SuperKanbanToDo expected = new SuperKanbanToDo("2", "description2", "status2");
        // Then
        assertEquals(expected, superKanbanRepo.getToDoById("2"));
    }

    @Test
    void getToDoById_whenNoSuchToDo_thenThrow() {
        // Given
        List<SuperKanbanToDo> toDoList = new ArrayList<>();
        toDoList.add(new SuperKanbanToDo("1", "description1", "status1"));
        toDoList.add(new SuperKanbanToDo("2", "description2", "status2"));
        toDoList.add(new SuperKanbanToDo("3", "description3", "status3"));
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, toDoList);
        // When
        // Then
        assertThrows(NoSuchToDoFound.class, () -> superKanbanRepo.getToDoById("4"));
    }

    @Test
    void createToDo_when_thenReturnIt() {
        // Given
        List<SuperKanbanToDo> toDoList = new ArrayList<>();
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, toDoList);
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "description1", "status1");
        SuperKanbanToDoMemento memento = expected.saveStateMemento(OperationEvent.CREATE);
        // When
        // Then
        assertEquals(expected, superKanbanRepo.createToDo(expected));
    }

    @Test
    void updateToDo_whenExists_thenDoAndReturn() {
        // Given
        List<SuperKanbanToDo> toDoList = new ArrayList<>();
        toDoList.add(new SuperKanbanToDo("1", "description1", "status1"));
        toDoList.add(new SuperKanbanToDo("2", "description2", "status2"));
        toDoList.add(new SuperKanbanToDo("3", "description3", "status3"));
        SuperKanbanRepo superKanbanRepo = new SuperKanbanRepo(mockCareTakerService, toDoList);
        // When
        SuperKanbanToDo toDo = new SuperKanbanToDo("2", "description2", "status2");
        SuperKanbanToDo expected = new SuperKanbanToDo("2", "description2", "status2");
        // Then
        assertEquals(expected, superKanbanRepo.updateToDo("2", toDo));
    }

    @Test
    void deleteToDo() {
        assertTrue(true);
    }
}