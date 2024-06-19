package org.example.backend.codeeditor.service;

import org.example.backend.codeeditor.entity.UserEntity;
import org.example.backend.codeeditor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //새로운 사용자 생성
    public UserEntity createUser(String username) {
        UserEntity user = new UserEntity(username);
        return userRepository.save(user);
    }

    //id로 사용자 찾기
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //유저이름으로 사용자 찾기
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}