package com.example.testdataelasticsearch.controller;

import com.example.testdataelasticsearch.entity.User;
import com.example.testdataelasticsearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable final String id) {
        return userService.findById(id);
    }

    @PostMapping
    public void save(@RequestBody final User user) {
        userService.save(user);
    }
}
