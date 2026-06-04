package com.chat.bff.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import com.chat.bff.config.SecurityConfig;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageBffController.class)
@Import(SecurityConfig.class)
class MessageBffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private MessageClient messageClient;

    @Test
    void sendMessageUsesUserIdFromTokenAndReturns201() throws Exception {
        when(messageClient.sendMessage(42L, "Hello"))
                .thenReturn(new MessageResponse(1L, 42L, "Hello",
                        Instant.parse("2026-01-01T00:00:00Z"), "test_king"));

        mockMvc.perform(post("/api/messages")
                        .with(jwt().jwt(token -> token.subject("42")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new SendMessageRequest("Hello"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderId").value(42))
                .andExpect(jsonPath("$.senderUsername").value("test_king"));

        verify(messageClient).sendMessage(42L, "Hello");
    }

    @Test
    void sendMessageWithoutTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new SendMessageRequest("Hello"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMessagesReturnsListForAuthenticatedUser() throws Exception {
        when(messageClient.getMessages())
                .thenReturn(List.of(new MessageResponse(1L, 42L, "Hello",
                        Instant.parse("2026-01-01T00:00:00Z"), "test_king")));

        mockMvc.perform(get("/api/messages").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello"));
    }
}