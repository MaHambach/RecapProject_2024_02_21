package org.github.mahambach.recapproject_2024_02_21.service;

import org.github.mahambach.recapproject_2024_02_21.exception.NoChatGptResponse;
import org.github.mahambach.recapproject_2024_02_21.model.ChatGptRequest;
import org.github.mahambach.recapproject_2024_02_21.model.ChatGptResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGptService {
    private final RestClient client;

    public ChatGptService(@Value("${open.ai.key}") String apikey,
                          @Value("${open.ai.url}") String baseurl){
        client = RestClient.builder()
                .baseUrl(baseurl)
                .defaultHeader("Authorization", "Bearer " + apikey)
                .build();
    }

    public String spellCheck(String text){
        ChatGptRequest request = new ChatGptRequest("Bitte korrigiere Rechtschreibung und Grammatik und gib nur den Korrekten Text ohne zusätzliche Satzzeichen zurück: " + text);
        return postPrompt(request);
    }

    String postPrompt(ChatGptRequest request){
        ChatGptResponse response = client.post()
                .body(request)
                .retrieve()
                .body(ChatGptResponse.class);
        if(response == null){throw new NoChatGptResponse("Error: No response given by ChatGPT.");}
        if(response.getAnswer() == null){throw new NoChatGptResponse("Error: No response given by ChatGPT.");}
        return response.getAnswer();
    }
}
