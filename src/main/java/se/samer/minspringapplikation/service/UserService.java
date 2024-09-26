package se.samer.minspringapplikation.service;

import org.springframework.stereotype.Service;
import se.samer.minspringapplikation.model.Role;
import se.samer.minspringapplikation.model.User;
import se.samer.minspringapplikation.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User saveUserWithRole(User user, Long roleId) {
        // Hämta rollen och lägg till den till användarens roller
        Role role = roleService.findRoleById(roleId);
        user.addRole(role);  // Använd addRole-metoden istället för setRole
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
