package se.samer.minspringapplikation.client;

import org.springframework.web.client.RestTemplate;
import se.samer.minspringapplikation.model.User;

import java.util.Scanner;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/users"; // Justera bas-URL om nödvändigt
    private final RestTemplate restTemplate;

    public ApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public void createUser(String name, String email) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        User response = restTemplate.postForObject(BASE_URL, newUser, User.class);
        System.out.println("Created User: " + response);
    }

    public void getAllUsers() {
        User[] users = restTemplate.getForObject(BASE_URL, User[].class);
        if (users != null) {
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("No users found.");
        }
    }

    public void getUserById(Long id) {
        User user = restTemplate.getForObject(BASE_URL + "/" + id, User.class);
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
        restTemplate.put(BASE_URL + "/" + id, updatedUser);
        System.out.println("Updated User with ID: " + id);
    }

    public void deleteUser(Long id) {
        restTemplate.delete(BASE_URL + "/" + id);
        System.out.println("Deleted User with ID: " + id);
    }

    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create User");
            System.out.println("2. Get All Users");
            System.out.println("3. Get User by ID");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Exit");
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
                    Long id = scanner.nextLong();
                    client.getUserById(id);
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
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
