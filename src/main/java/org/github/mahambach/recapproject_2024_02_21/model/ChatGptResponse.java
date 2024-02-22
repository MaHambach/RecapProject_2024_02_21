package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptResponse {
    private List<ChatGptChoice> choices;

    public String getAnswer(){
        return getChoices().get(0).getMessage().getContent();
    }
}
