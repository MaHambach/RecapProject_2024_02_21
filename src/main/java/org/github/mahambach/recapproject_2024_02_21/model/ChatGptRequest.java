package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<ChatGptMessage> messages;

    public ChatGptRequest(String question){
        this.model = "gpt-3.5-turbo";
        this.messages = List.of(new ChatGptMessage(question));
    }
}
