package com.example.testdataelasticsearch.service;

import com.example.testdataelasticsearch.entity.User;
import com.example.testdataelasticsearch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(final User user) {
        userRepository.save(user);
    }

    public User findById(final String id) {
        return userRepository.findById(id)
                .orElse(null);
    }
}