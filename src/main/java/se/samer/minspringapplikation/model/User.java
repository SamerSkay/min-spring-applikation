package se.samer.minspringapplikation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters och setters för roller
    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)  // För att undvika lazy-loading problem
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Metod för att hantera roller
    public void addRole(Role role) {
        this.roles.add(role);
    }

    // toString() metoden
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles= " + roles.stream().map(Role::getName).collect(Collectors.joining(", ")) +
                '}';
    }
}