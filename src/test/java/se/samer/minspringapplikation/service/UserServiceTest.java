package se.samer.minspringapplikation.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.samer.minspringapplikation.model.Role;
import se.samer.minspringapplikation.model.User;
import se.samer.minspringapplikation.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(1L);

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void saveUser() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUserWithRole() {
        User user = new User();
        Role role = new Role();
        when(roleService.findRoleById(1L)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUserWithRole(user, 1L);

        assertTrue(savedUser.getRoles().contains(role));  // Kontrollera att rollen har lagts till
        verify(roleService, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
