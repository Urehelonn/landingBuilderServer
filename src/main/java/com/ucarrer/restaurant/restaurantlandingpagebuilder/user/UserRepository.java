package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<User> findByGoogleId(String googleId);
}