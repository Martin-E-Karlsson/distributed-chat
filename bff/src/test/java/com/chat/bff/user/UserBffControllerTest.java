package com.chat.bff.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import com.chat.bff.auth.AuthClient;
import com.chat.bff.config.SecurityConfig;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserBffController.class)
@Import(SecurityConfig.class)
class UserBffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private AuthClient authClient;

    @MockitoBean
    private UserClient userClient;

    @Test
    void createUserRegistersCredentialAndCreatesProfile() throws Exception {
        when(userClient.createUser("test_king", "King_of_Tests"))
                .thenReturn(new UserResponse(1L, "test_king", "King_of_Tests"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new CreateUserRequest("test_king", "s3cret", "King_of_Tests"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_king"))
                .andExpect(jsonPath("$.displayname").value("King_of_Tests"));

        verify(authClient).register("test_king", "s3cret");
        verify(userClient).createUser("test_king", "King_of_Tests");
    }
}
