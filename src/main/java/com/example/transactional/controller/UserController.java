package com.example.transactional.controller;

import com.example.transactional.model.User;
import com.example.transactional.service.CustomException;
import com.example.transactional.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(service.updateUser(id, user));
    }

    @PostMapping("/test1")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(service.addUser(user));
    }

    @PostMapping("/test2")
    public ResponseEntity<User> addNewUser(@RequestBody User user) throws CustomException {
        return ResponseEntity.ok(service.addUserThrowsCustomException(user));
    }


}
