package org.github.mahambach.recapproject_2024_02_21.service;

import org.github.mahambach.recapproject_2024_02_21.exception.NoChatGptResponse;
import org.github.mahambach.recapproject_2024_02_21.exception.NoSuchToDoFound;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoDTO;
import org.github.mahambach.recapproject_2024_02_21.repositories.SuperKanbanRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SuperKanbanServiceTest {
    private final SuperKanbanRepo mockSuperKanbanRepo = mock(SuperKanbanRepo.class);
    private final IdService mockIdService = mock(IdService.class);
    private final ChatGptService mockChatGptService = mock(ChatGptService.class);

    @Test
    void testGetAllToDos_whenEmpty_thenEmpty() {
        // given
        List<SuperKanbanToDo> expected = new ArrayList<>();
        when(mockSuperKanbanRepo.getAllToDos()).thenReturn(new ArrayList<>());

        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.getAllToDos());
        verify(mockSuperKanbanRepo, times(1)).getAllToDos();
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void testGetAllToDos_whenList_thenList() {
        // given
        List<SuperKanbanToDo> expected = new ArrayList<>();
        expected.add(new SuperKanbanToDo("1", "description1", "status1"));
        expected.add(new SuperKanbanToDo("2", "description2", "status2"));
        expected.add(new SuperKanbanToDo("3", "description3", "status3"));

        when(mockSuperKanbanRepo.getAllToDos()).thenReturn(expected);

        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.getAllToDos());
        verify(mockSuperKanbanRepo, times(1)).getAllToDos();
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void testGetToDoById() {
        // given
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "description1", "status1");
        when(mockSuperKanbanRepo.getToDoById("1")).thenReturn(expected);

        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.getToDoById("1"));
        verify(mockSuperKanbanRepo, times(1)).getToDoById("1");
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void createToDo() {
        // given
        SuperKanbanToDoDTO toDoDTO = new SuperKanbanToDoDTO("description1", "status1");
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "Description 1", "status1");
        when(mockSuperKanbanRepo.createToDo(any(SuperKanbanToDo.class))).thenReturn(expected);
        when(mockIdService.generateId()).thenReturn("1");
        when(mockChatGptService.spellCheck("description1")).thenReturn("Description 1");
        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.createToDo(toDoDTO));
        verify(mockSuperKanbanRepo, times(1)).createToDo(any(SuperKanbanToDo.class));
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void createToDo_whenChatGptServiceError_thenThrowNoChatGptResponse() {
        // given
        SuperKanbanToDoDTO toDoDTO = new SuperKanbanToDoDTO("description1", "status1");
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "Description 1", "status1");
        when(mockSuperKanbanRepo.createToDo(expected)).thenReturn(expected);
        when(mockIdService.generateId()).thenReturn("1");
        when(mockChatGptService.spellCheck("description1")).thenThrow(new NoChatGptResponse("Error: No response given by ChatGPT."));
        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertThrows(NoChatGptResponse.class, () -> superKanbanService.createToDo(toDoDTO));
        verifyNoInteractions(mockSuperKanbanRepo);
    }

    @Test
    void updateToDo_whenExists_thenDo() {
        // given
        SuperKanbanToDo toDo = new SuperKanbanToDo("1", "description1", "status1");
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "description1", "status1");
        when(mockSuperKanbanRepo.updateToDo("1", toDo)).thenReturn(expected);
        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.updateToDo("1", toDo));
        verify(mockSuperKanbanRepo, times(1)).updateToDo("1", toDo);
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }
    @Test
    void updateToDo_whenNotExist_thenThrow() {
        // given
        SuperKanbanToDo toDo = new SuperKanbanToDo("1", "description1", "status1");
        when(mockSuperKanbanRepo.updateToDo("1", toDo)).thenThrow(new NoSuchToDoFound("1"));
        // When
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);
        // Then
        assertThrows(NoSuchToDoFound.class, () -> superKanbanService.updateToDo("1", toDo));
        verify(mockSuperKanbanRepo, times(1)).updateToDo("1", toDo);
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }
    @Test
    void updateToDo_whenMissMatchingIds_thenThrow() {
        // Given
        SuperKanbanToDo toDo = new SuperKanbanToDo("2", "description1", "status1");
        when(mockSuperKanbanRepo.updateToDo("1", toDo)).thenThrow(new IllegalArgumentException("Id in path '1' and id in body '2' do not match!"));
        // When
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);
        // Then
        assertThrows(IllegalArgumentException.class, () -> superKanbanService.updateToDo("1", toDo));
        verify(mockSuperKanbanRepo, times(1)).updateToDo("1", toDo);
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void deleteToDo_whenExists_thenDeleteAndReturn() {
        // given
        SuperKanbanToDo expected = new SuperKanbanToDo("1", "description1", "status1");
        when(mockSuperKanbanRepo.deleteToDo("1")).thenReturn(expected);

        // when
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);

        // then
        assertEquals(expected, superKanbanService.deleteToDo("1"));
        verify(mockSuperKanbanRepo, times(1)).deleteToDo("1");
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }

    @Test
    void deleteToDo_whenNoSuchId_thenThrow() {
        // Given
        when(mockSuperKanbanRepo.deleteToDo("1")).thenThrow(new NoSuchToDoFound("1"));
        // When
        SuperKanbanService superKanbanService = new SuperKanbanService(mockSuperKanbanRepo, mockChatGptService, mockIdService);
        // Then
        assertThrows(NoSuchToDoFound.class, () -> superKanbanService.deleteToDo("1"));
        verify(mockSuperKanbanRepo, times(1)).deleteToDo("1");
        verifyNoMoreInteractions(mockSuperKanbanRepo);
    }
}