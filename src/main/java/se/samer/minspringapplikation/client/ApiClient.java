package se.samer.minspringapplikation.client;

import org.springframework.web.client.RestTemplate;
import se.samer.minspringapplikation.model.User;
import se.samer.minspringapplikation.model.Role;

import java.util.Scanner;

public class ApiClient {

    private static final String BASE_USERS_URL = "http://localhost:8080/api/users"; // bas-url för användare
    private static final String BASE_ROLES_URL = "http://localhost:8080/api/roles"; // bas-url för roller
    private final RestTemplate restTemplate;

    public ApiClient() {
        this.restTemplate = new RestTemplate();
    }

    /////////////////////////// ANVÄNDARE /////////////////////////////////////

    public void createUser(String name, String email) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        User response = restTemplate.postForObject(BASE_USERS_URL, newUser, User.class);
        System.out.println("Created User: " + response);
    }

    public void getAllUsers() {
        User[] users = restTemplate.getForObject(BASE_USERS_URL, User[].class);
        if (users != null) {
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("No users found.");
        }
    }

    public void getUserById(Long id) {
        User user = restTemplate.getForObject(BASE_USERS_URL + "/" + id, User.class);
        if (user != null) {
            System.out.println("Retrieved User: " + user);
        } else {
            System.out.println("User not found.");
        }
    }

    public void updateUser(Long id, String name, String email) {
        User updatedUser = new User();
        updatedUser.setName(name);
        updatedUser.setEmail(email);
        restTemplate.put(BASE_USERS_URL + "/" + id, updatedUser);
        System.out.println("Updated User with ID: " + id);
    }

    public void deleteUser(Long id) {
        restTemplate.delete(BASE_USERS_URL + "/" + id);
        System.out.println("Deleted User with ID: " + id);
    }

    /////////////////////////// ROLLER /////////////////////////////////////

    public void createRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        Role response = restTemplate.postForObject(BASE_ROLES_URL, newRole, Role.class);
        System.out.println("Created Role: " + response);
    }

    public void getAllRoles() {
        Role[] roles = restTemplate.getForObject(BASE_ROLES_URL, Role[].class);
        if (roles != null) {
            for (Role role : roles) {
                System.out.println(role);
            }
        } else {
            System.out.println("No roles found.");
        }
    }

    public void getRoleById(Long id) {
        Role role = restTemplate.getForObject(BASE_ROLES_URL + "/" + id, Role.class);
        if (role != null) {
            System.out.println("Retrieved Role: " + role);
        } else {
            System.out.println("Role not found.");
        }
    }

    /////////////////////////// KOPPLA ANVÄNDARE OCH ROLLER /////////////////////////////////////

    public void addRoleToUser(Long userId, Long roleId) {
        restTemplate.put(BASE_USERS_URL + "/" + userId + "/roles/" + roleId, null);
        System.out.println("Added role with ID: " + roleId + " to user with ID: " + userId);
    }

    /////////////////////////// KONSOLPROGRAM /////////////////////////////////////

    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- API Client Users/Roles ---");
            System.out.println("1. Create User");
            System.out.println("2. Get All Users");
            System.out.println("3. Get User by ID");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Create Role");
            System.out.println("7. Get All Roles");
            System.out.println("8. Add Role to User");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    client.createUser(name, email);
                    break;
                case 2:
                    client.getAllUsers();
                    break;
                case 3:
                    System.out.print("Enter User ID: ");
                    Long userId = scanner.nextLong();
                    client.getUserById(userId);
                    break;
                case 4:
                    System.out.print("Enter User ID to update: ");
                    Long updateId = scanner.nextLong();
                    scanner.nextLine(); // Clear the buffer
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    client.updateUser(updateId, newName, newEmail);
                    break;
                case 5:
                    System.out.print("Enter User ID to delete: ");
                    Long deleteId = scanner.nextLong();
                    client.deleteUser(deleteId);
                    break;
                case 6:
                    System.out.print("Enter Role name: ");
                    String roleName = scanner.nextLine();
                    client.createRole(roleName);
                    break;
                case 7:
                    client.getAllRoles();
                    break;
                case 8:
                    System.out.print("Enter User ID: ");
                    Long userToAddRole = scanner.nextLong();
                    System.out.print("Enter Role ID: ");
                    Long roleIdToAdd = scanner.nextLong();
                    client.addRoleToUser(userToAddRole, roleIdToAdd);
                    break;
                case 9:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
