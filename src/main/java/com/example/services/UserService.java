package com.example.services;


import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(User user)
    {
        if (!userRepository.existsByUsername(user.getUsername()))
        {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    public void save(User user)
    {
        userRepository.save(user);
    }

    public List<User> findAll()
    {
        Iterable<User> allUsers = userRepository.findAll();
        List<User> users = new ArrayList<>();
        for (User user : allUsers)
        {
            users.add(user);
        }
        return users;
    }

    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }


    public User findUserById(Long id)
    {
        return userRepository.findUserById(id);
    }
}
