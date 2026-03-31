package com.polaris.api.controller;

import com.polaris.api.model.User;
import com.polaris.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin operations - user management")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ═══════════════════════════════════════════════════════════════════
    // POST /api/admin/users - Create new user
    // ═══════════════════════════════════════════════════════════════════
    @PostMapping("/users")
    @Operation(summary = "Create new user", description = "Creates a new user with role (ADMIN or USER)")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String role = request.get("role");

        // Validation
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Username is required"));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Password is required"));
        }

        // Check duplicate
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Username already exists: " + username));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email != null ? email : username + "@polaris-tpa.com");
        user.setRole(role != null ? role.toUpperCase() : "USER");

        User saved = userRepository.save(user);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "User created successfully");
        response.put("user", Map.of(
                "id", saved.getId(),
                "username", saved.getUsername(),
                "email", saved.getEmail(),
                "role", saved.getRole()
        ));

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/admin/users - List all users
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Returns all registered users (passwords excluded)")
    public ResponseEntity<?> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<Map<String, Object>> data = users.stream()
                .map(u -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", u.getId());
                    map.put("username", u.getUsername());
                    map.put("email", u.getEmail());
                    map.put("role", u.getRole());
                    map.put("last_login", u.getLastLogin() != null ? u.getLastLogin().toString() : null);
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
