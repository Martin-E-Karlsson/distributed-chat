package com.chat.userservice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void createUserReturns201AndBody() throws Exception {
        when(userService.create("test_king", "King_of_Tests"))
                .thenReturn(new User(1L, "test_king", "King_of_Tests"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new CreateUserRequest("test_king", "King_of_Tests"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_king"))
                .andExpect(jsonPath("$.displayname").value("King_of_Tests"));
    }

    @Test
    void createUserWithBlankUsernameReturns400() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"displayname\":\"King_of_Tests\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithDuplicateUsernameReturns409() throws Exception {
        when(userService.create("test_king", "King_of_Tests"))
                .thenThrow(new DuplicateUsernameException("test_king"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(
                                new CreateUserRequest("test_king", "King_of_Tests"))))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserReturns200AndBody() throws Exception {
        when(userService.getById(1L))
                .thenReturn(new User(1L, "test_king", "King_of_Tests"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_king"));
    }

    @Test
    void getMissingUserReturns404() throws Exception {
        when(userService.getById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }
}
