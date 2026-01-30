package com.lescours.bobolognese.repository;

import com.lescours.bobolognese.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class UserRepository extends GenericRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public User saveOrUpdate(User user) {
        if (user.getId() == null) {
            return save(user);
        } else {
            return update(user);
        }
    }
}
