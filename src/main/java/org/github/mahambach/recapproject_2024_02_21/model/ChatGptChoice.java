package org.github.mahambach.recapproject_2024_02_21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptChoice {

    /**
     * {
     *     "message": {
     *         "content": "This is a test!"
     *     }
     * }
     */
    private ChatGptMessage message;
}