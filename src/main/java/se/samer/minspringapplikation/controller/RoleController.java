package se.samer.minspringapplikation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.samer.minspringapplikation.model.Role;
import se.samer.minspringapplikation.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.findRoleById(id);
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role role = roleService.findRoleById(id);

        if (role == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Uppdatera rollens namn
        role.setName(roleDetails.getName());

        // Spara den uppdaterade rollen
        Role updatedRole = roleService.saveRole(role);
        return ResponseEntity.ok(updatedRole);
    }


    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}