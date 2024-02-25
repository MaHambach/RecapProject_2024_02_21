package org.github.mahambach.recapproject_2024_02_21.repositories;

import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDo;
import org.github.mahambach.recapproject_2024_02_21.model.SuperKanbanToDoMemento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperKanbanRepo extends MongoRepository<SuperKanbanToDo, String> {

}
