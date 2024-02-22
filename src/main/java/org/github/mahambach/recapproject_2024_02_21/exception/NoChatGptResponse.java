package org.github.mahambach.recapproject_2024_02_21.exception;

public class NoChatGptResponse extends RuntimeException{
    public NoChatGptResponse(String message){
        super(message);
    }
}
