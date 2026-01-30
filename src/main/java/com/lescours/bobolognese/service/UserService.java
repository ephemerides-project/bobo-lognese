package com.lescours.bobolognese.service;

import com.lescours.bobolognese.model.UserRole;
import com.lescours.bobolognese.model.User;
import com.lescours.bobolognese.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService implements Serializable {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordService passwordService;

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordService.verifyPassword(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User create(String username, String password) {
        return create(username, password, UserRole.VIEWER);
    }

    public User create(String username, String password, UserRole role) {
        String hashedPassword = passwordService.hashPassword(password);
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .role(role)
                .build();
        return userRepository.saveOrUpdate(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
