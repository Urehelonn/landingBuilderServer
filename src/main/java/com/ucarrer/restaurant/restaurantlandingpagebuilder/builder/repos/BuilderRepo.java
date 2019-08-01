package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.repos;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuilderRepo extends JpaRepository<Builder, Long> {

}