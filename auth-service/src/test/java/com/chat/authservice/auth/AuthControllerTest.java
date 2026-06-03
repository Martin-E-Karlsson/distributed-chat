package com.chat.authservice.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;
import com.chat.authservice.credential.Credential;
import com.chat.authservice.credential.CredentialService;
import com.chat.authservice.credential.DuplicateUsernameException;

import com.chat.authservice.config.SecurityConfig;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, AuthExceptionHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CredentialService credentialService;

    @Test
    void loginReturns200AndToken() throws Exception {
        when(authService.login("test_king", "s3cret")).thenReturn("the-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new LoginRequest("test_king", "s3cret"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("the-token"));
    }

    @Test
    void loginReturns401ForBadCredentials() throws Exception {
        when(authService.login("test_king", "wrong"))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new LoginRequest("test_king", "wrong"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerReturns201AndBody() throws Exception {
        when(credentialService.register("test_king", "s3cret"))
                .thenReturn(new Credential(1L, "test_king", "hash"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new RegisterRequest("test_king", "s3cret"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_king"));
    }

    @Test
    void registerReturns409ForDuplicate() throws Exception {
        when(credentialService.register("test_king", "s3cret"))
                .thenThrow(new DuplicateUsernameException("test_king"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new RegisterRequest("test_king", "s3cret"))))
                .andExpect(status().isConflict());
    }
}
