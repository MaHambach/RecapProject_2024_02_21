package org.github.mahambach.recapproject_2024_02_21.controller;

import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.repositories.SuperKanbanRepo;
import org.github.mahambach.recapproject_2024_02_21.service.ChatGptService;
import org.github.mahambach.recapproject_2024_02_21.service.IdService;
import org.github.mahambach.recapproject_2024_02_21.service.SuperKanbanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SuperKanbanControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SuperKanbanService superKanbanService;

    @MockBean
    private ChatGptService chatGptService;

    @Test
    void getAllToDos_whenEmpty_thenEmpty() throws Exception {
        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        []
                        """));
    }

    @Test
    void getAllToDos_whenSomething_thenSomething() throws Exception {
        // Given
        when(superKanbanService.getAllToDos()).thenReturn(List.of(
                new SuperKanbanToDo("1", "description1", "OPEN"),
                new SuperKanbanToDo("2", "description2", "IN_PROGRESS"),
                new SuperKanbanToDo("3", "description3", "DONE")
        ));

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": "1",
                                "description":"description1",
                                "status":"OPEN"
                            },
                            {
                                "id": "2",
                                "description":"description2",
                                "status":"IN_PROGRESS"
                            },
                            {
                                "id": "3",
                                "description":"description3",
                                "status":"DONE"
                            }
                        ]
                        """));
    }

    @Test
    void getToDoById_whenIdExists_thenReturnToDo() throws Exception {
        //Given
        when(superKanbanService.getToDoById("1")).thenReturn(new SuperKanbanToDo("1", "description1", "OPEN"));

        //When & Then
        mvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "1",
                            "description":"description1",
                            "status":"OPEN"
                        }
                        """));
    }

    @Test
    void getToDoById_whenFalseId_thenError() throws Exception {
        // Given
        when(superKanbanService.getToDoById("2")).thenThrow(new NoSuchToDoFound("2"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/api/todo/2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "apiPath": "uri=/api/todo/2",
                            "errorCode": "BAD_REQUEST",
                            "errorMsg": "No ToDo with id '2' found."
                        }
                        """));
    }

    @Test
    void createToDo() throws Exception {
        // Given
        when(superKanbanService.createToDo(new SuperKanbanToDoDTO("description1", "OPEN")))
                .thenReturn(new SuperKanbanToDo("1", "description1", "OPEN"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description":"description1",
                            "status":"OPEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "1",
                            "description": "description1",
                            "status": "OPEN"
                        }
                        """
                ));
    }

    @Test
    void updateToDo_whenExistingToDo_thenUpdate() throws Exception {
        // Given
        when(superKanbanService.updateToDo("1", new SuperKanbanToDo("1", "currywurst", "OPEN")))
                .thenReturn(new SuperKanbanToDo("1", "currywurst", "OPEN"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description":"currywurst",
                            "id":"1",
                            "status":"OPEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id":"1",
                            "description":"currywurst",
                            "status":"OPEN"
                        }
                        """
                ));
    }

    @Test
    void updateToDo_whenMissMatchingIds_thenThrow() throws Exception {
        // Given
        when(superKanbanService.updateToDo("1", new SuperKanbanToDo("2", "currywurst", "OPEN")))
                .thenThrow(new NoSuchToDoFound("2"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description":"currywurst",
                            "id":"2",
                            "status":"OPEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "apiPath": "uri=/api/todo/1",
                            "errorCode": "BAD_REQUEST",
                            "errorMsg": "No ToDo with id '2' found."
                        }
                        """
                ));
    }

    @Test
    void updateToDo_whenNoSuchId_thenThrow() throws Exception {
        // Given
        when(superKanbanService.updateToDo("1", new SuperKanbanToDo("2", "currywurst", "OPEN")))
                .thenThrow(new IllegalArgumentException("Id in path '1' and id in body '2' do not match!"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.put("/api/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description":"currywurst",
                            "id":"2",
                            "status":"OPEN"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "apiPath": "uri=/api/todo/1",
                            "errorCode": "BAD_REQUEST",
                            "errorMsg": "Id in path '1' and id in body '2' do not match!"
                        }
                        """
                ));
    }

    @Test
    void deleteToDo_whenExists_thenDeletAndReturnDeleted() throws Exception {
        // Given
        when(superKanbanService.deleteToDo("1")).thenReturn(new SuperKanbanToDo("1", "description1", "OPEN"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/todo/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "1",
                            "description":"description1",
                            "status":"OPEN"
                        }
                        """
                ));
    }

    @Test
    void deleteToDo_whenNoSuchToDo_thenThrow() throws Exception {
        // Given
        when(superKanbanService.deleteToDo("2")).thenThrow(new NoSuchToDoFound("2"));
        // When & Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/todo/2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "apiPath": "uri=/api/todo/2",
                            "errorCode": "BAD_REQUEST",
                            "errorMsg": "No ToDo with id '2' found."
                        }
                        """
                ));
    }
}