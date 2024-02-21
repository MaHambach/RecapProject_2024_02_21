package org.github.mahambach.recapproject_2024_02_21.exception;

public class NoSuchToDoFound extends RuntimeException{
    public NoSuchToDoFound(String id) {
        super("No ToDo with id '" + id + "' found.");
    }
}
