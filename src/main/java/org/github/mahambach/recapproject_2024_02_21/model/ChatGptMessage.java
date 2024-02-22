package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptMessage {

    /**
     * {
     *     "role": "user",
     *     "content": "Say this is a test!"
     *  }
     */

    private String role;
    private String content;

    public ChatGptMessage(String question){
        this.role = "user";
        this.content = question;
    }

}