package org.github.mahambach.recapproject_2024_02_21.service;

import lombok.RequiredArgsConstructor;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareTakerService {
    private final List<SuperKanbanToDoMemento> undoList;
    private final List<SuperKanbanToDoMemento> redoList;

    public void add(SuperKanbanToDoMemento state) {
        undoList.add(state);
        redoList.clear();
    }

    public SuperKanbanToDoMemento get(int index) {
        return undoList.get(index);
    }

    public SuperKanbanToDoMemento getLast() {
        return undoList.getLast();
    }

    public boolean isEmpty() {
        return undoList.isEmpty();
    }

    public SuperKanbanToDoMemento undo() {
        if (undoList.isEmpty()) {
            throw new IllegalStateException("No more undo possible!");
        }
        SuperKanbanToDoMemento lastState = undoList.removeLast();

        redoList.add(lastState);

        return lastState;
    }

    public SuperKanbanToDoMemento redo() {
        if (redoList.isEmpty()) {
            throw new IllegalStateException("No more redo possible!");
        }
        SuperKanbanToDoMemento lastState = redoList.removeLast();

        undoList.add(lastState);

        return lastState;
    }
}
