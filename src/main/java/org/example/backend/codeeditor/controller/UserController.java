package org.example.backend.codeeditor.controller;

import org.example.backend.codeeditor.entity.UserEntity;
import org.example.backend.codeeditor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 생성
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestParam("username") String username) {
        UserEntity user = userService.createUser(username);
        return ResponseEntity.ok(user);
    }

    // 사용자 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 이름으로 조회
    @GetMapping("/username/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        Optional<UserEntity> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
