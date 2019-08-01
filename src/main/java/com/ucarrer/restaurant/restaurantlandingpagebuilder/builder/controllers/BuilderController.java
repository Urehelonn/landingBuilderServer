package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.controllers;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.Builder;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.Gallery;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.GalleryItem;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.Head;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.repos.BuilderRepo;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.repos.HeadRepo;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/builder")
public class BuilderController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BuilderRepo builderRepo;

    @Autowired
    HeadRepo headRepo;

    @GetMapping("/test")
    public ResponseEntity<CoreResponseBody> test(){
        CoreResponseBody response = new CoreResponseBody("test", "test", null);

        Long userId = 1L;

        User user = userRepository.findById(userId).orElse(null);
        Builder builder = new Builder();
        builder.setName("Longs food123");
        builder.setUser(user);
        userRepository.save(user);



        Head head = new Head();
        head.setTitle("Long title");
        head.setSubtitle("Long subtitle");
        head.setBackgroundImgUrl("https://res.cloudinary.com/dx55oi3py/image/upload/v1564456289/images/bg4.png");
        head.setBuilder(builder);
        builder.setHead(head);

        Gallery gallery = new Gallery();
        gallery.setTitle("long gallery title");
        gallery.setBuilder(builder);

        GalleryItem item1 = new GalleryItem();
        item1.setTitle("gallery item 1");
        item1.setGallery(gallery);
        GalleryItem item2 = new GalleryItem();
        item2.setTitle("gallery item 2");
        item2.setGallery(gallery);

        Set<GalleryItem> items = new HashSet<GalleryItem>();
        items.add(item1);
        items.add(item2);
        gallery.setGalleryItems(items);

        builder.setGallery(gallery);
        builderRepo.save(builder);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<CoreResponseBody> get(){


        Long userId = 1L;

        User user = userRepository.findById(userId).orElse(null);
        CoreResponseBody response = new CoreResponseBody(user, "test", null);
        return ResponseEntity.ok(response);
    }

}
