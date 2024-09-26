package se.samer.minspringapplikation.controller;

import org.springframework.web.bind.annotation.*;
import se.samer.minspringapplikation.model.User;
import se.samer.minspringapplikation.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/role/{roleId}")
    public User createUserWithRole(@RequestBody User user, @PathVariable Long roleId) {
        return userService.saveUserWithRole(user, roleId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}