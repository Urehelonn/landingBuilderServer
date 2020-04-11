package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuilderRepository extends JpaRepository<Builder, Long> {
    Optional<Builder> findByUser(User user);
    Optional<Builder> findById(long id);
}
