package org.github.mahambach.recapproject_2024_02_21.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.github.mahambach.recapproject_2024_02_21.exception.NoChatGptResponse;
import org.github.mahambach.recapproject_2024_02_21.model.ChatGptRequest;
import org.github.mahambach.recapproject_2024_02_21.model.ChatGptResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ChatGptServiceTest {
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void backedProperties(DynamicPropertyRegistry registry) {
        registry.add("${open.ai.url}", () -> mockWebServer.url("/").toString());
    }

    @Test
    void postPrompt_whenEverythingWorks_thenReturnContentBrain() {
        // Given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "id": "chatcmpl-8uzWJDwLJokrKyjr35PRWgGYHoTtA",
                            "object": "chat.completion",
                            "created": 1708593543,
                            "model": "gpt-3.5-turbo-0125",
                            "choices": [
                                {
                                    "index": 0,
                                    "message": {
                                        "role": "assistant",
                                        "content": "Brain"
                                    },
                                    "logprobs": null,
                                    "finish_reason": "stop"
                                }
                            ],
                            "usage": {
                                "prompt_tokens": 18,
                                "completion_tokens": 5,
                                "total_tokens": 23
                            },
                            "system_fingerprint": "fp_cbdb91ce3f"
                        }
                        """)
                .addHeader("Content-Type", "application/json")
        );
        String expected = "Brain";
        // When & Then
        ChatGptService chatGptService = new ChatGptService("apikey", mockWebServer.url("/").toString());
        assertEquals(expected, chatGptService.postPrompt(new ChatGptRequest("prompt")));

    }

    @Test
    void postPrompt_whenNoContent_thenThrow() {
        // Given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        {
                            "id": "chatcmpl-8uzWJDwLJokrKyjr35PRWgGYHoTtA",
                            "object": "chat.completion",
                            "created": 1708593543,
                            "model": "gpt-3.5-turbo-0125",
                            "choices": [
                                {
                                    "index": 0,
                                    "message": {
                                        "role": "assistant"
                                    },
                                    "logprobs": null,
                                    "finish_reason": "stop"
                                }
                            ],
                            "usage": {
                                "prompt_tokens": 18,
                                "completion_tokens": 5,
                                "total_tokens": 23
                            },
                            "system_fingerprint": "fp_cbdb91ce3f"
                        }
                        """)
                .addHeader("Content-Type", "application/json")
        );
        String expected = "Brain";
        ChatGptRequest chatGptRequest = new ChatGptRequest("prompt");

        // When & Then
        ChatGptService chatGptService = new ChatGptService("apikey", mockWebServer.url("/").toString());
        assertThrows(NoChatGptResponse.class, () -> chatGptService.postPrompt(chatGptRequest));
    }

    @Test
    void postPrompt_whenResponseNull_thenThrow() {
        // Given
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
        );
        String expected = "Brain";
        ChatGptRequest chatGptRequest = new ChatGptRequest("prompt");

        // When & Then
        ChatGptService chatGptService = new ChatGptService("apikey", mockWebServer.url("/").toString());
        assertThrows(NoChatGptResponse.class, () -> chatGptService.postPrompt(chatGptRequest));
    }

}