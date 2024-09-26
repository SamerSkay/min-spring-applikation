package se.samer.minspringapplikation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    // Lägg till en metod för att hantera roller
    public void addRole(Role role) {
        this.roles.add(role);
    }

}
