package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.repos;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryItemRepo extends JpaRepository<GalleryItem, Long> {

}