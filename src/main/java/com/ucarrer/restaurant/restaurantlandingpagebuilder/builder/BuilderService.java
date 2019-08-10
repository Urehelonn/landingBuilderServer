package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("BuilderService")
public class BuilderService {
    @Autowired
    BuilderRepository repository;

    public Builder getBuilderByUser(User user){
        Builder builder = repository.findByUser(user).orElse(null);
        return builder;
    }

    public Builder updateBuilder(Builder builderInDb, Builder newBuilder){
        builderInDb.setGallery(builderInDb.getGallery());
        builderInDb.setHead(builderInDb.getHead());
        builderInDb.setMenu(builderInDb.getMenu());

        Builder saveUpdateBuilder = repository.save(builderInDb);

        return saveUpdateBuilder;
    }

    public Builder getBuilderById(Long id){
        Builder builder = repository.findById(id).orElse(null);
        return builder;
    }
}
