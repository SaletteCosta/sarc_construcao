package com.sarc.user.controller;

import com.sarc.domain.User;
import com.sarc.user.dto.UserDTO;
import com.sarc.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "List all users")
    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
