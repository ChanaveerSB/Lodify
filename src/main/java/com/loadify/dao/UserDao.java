package com.loadify.dao;

import com.loadify.entity.User;
import com.loadify.exception.UserNotFoundException;
import com.loadify.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) { return userRepository.save(user); }
    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
