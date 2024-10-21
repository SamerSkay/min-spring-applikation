package se.samer.minspringapplikation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.samer.minspringapplikation.model.User;
import se.samer.minspringapplikation.service.UserService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService; // Mocka UserService

    @InjectMocks
    private UserController userController; // Injektera mockade UserService i UserController

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@example.com");

        when(userService.findAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("user1@example.com");

        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User 1"));
    }

    @Test
    void createUser() throws Exception {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");

        User createdUser = new User();
        createdUser.setId(3L); // Anta att det skapade användarens ID är 3
        createdUser.setName(newUser.getName());
        createdUser.setEmail(newUser.getEmail());

        when(userService.saveUser(newUser)).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void createUserWithRole() throws Exception {
        User newUser = new User();
        newUser.setName("User with Role");
        newUser.setEmail("userwithrole@example.com");

        User createdUser = new User();
        createdUser.setId(4L); // Anta att det skapade användarens ID är 4
        createdUser.setName(newUser.getName());
        createdUser.setEmail(newUser.getEmail());

        when(userService.saveUserWithRole(newUser, 1L)).thenReturn(createdUser);

        mockMvc.perform(post("/api/users/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("User with Role"));
    }

    @Test
    void addRoleToUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("user1@example.com");

        when(userService.findUserById(1L)).thenReturn(user);
        when(userService.saveUserWithRole(user, 1L)).thenReturn(user);

        mockMvc.perform(put("/api/users/1/roles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L); // Gör inget när deleteUser kallas

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
