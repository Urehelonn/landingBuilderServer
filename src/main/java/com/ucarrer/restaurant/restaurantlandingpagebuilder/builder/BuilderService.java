package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("BuilderService")
public class BuilderService {
    @Autowired
    BuilderRepository repository;

    @Autowired
    UserRepository userRepository;

    public Builder getBuilderByUser(User user) {
        if (user.getBuilder() == null) {
           /* Builder newBuilder = repository.save(new Builder());
            newBuilder.setUser(user);
            user.setBuilder(newBuilder);
            userRepository.save(user);
            return user.getBuilder();*/
            return null;
        }
        Builder builder = repository.findById(user.getBuilder().getId()).orElse(null);
        if (builder == null) {
            System.out.println("builder service get null");
        }
        return builder;
    }

    public Builder getBuilderById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Builder save(Builder builder, User user) {
        Builder b = user.getBuilder();
        if (b == null) {
            b = new Builder();
            b.setName(builder.getName());

            Head head = new Head();
            head.setTitle(builder.getHead().getTitle());
            head.setDescription(builder.getHead().getDescription());
            head.setImgUrl(builder.getHead().getImgUrl());
            head.setBackground(builder.getHead().getBackground());
            b.setHead(head);

            Gallery gallery = new Gallery();
            gallery.setTitle(builder.getGallery().getTitle());
            gallery.setDescription(builder.getGallery().getDescription());
            gallery.setBackground(builder.getGallery().getBackground());
            gallery.setBuilder(b);

            if (builder.getGallery().getGalleryItems() != null && builder.getGallery().getGalleryItems().size() > 0) {
                List<GalleryItems> galleryItemsList = new ArrayList<GalleryItems>();
                for (GalleryItems item : builder.getGallery().getGalleryItems()) {
                    item.setGallery(gallery);
                    galleryItemsList.add(item);
                }
                gallery.setGalleryItems(galleryItemsList);
            }
            b.setGallery(gallery);

            Menu menu = new Menu();
            menu.setTitle(builder.getMenu().getTitle());
            menu.setDescription(builder.getMenu().getDescription());
            List<MenuItem> menuItemList = new ArrayList<MenuItem>();
            if (builder.getMenu().getMenuItems() != null && builder.getMenu().getMenuItems().size() > 0) {
                for (MenuItem item : builder.getMenu().getMenuItems()) {
                    item.setMenu(menu);
                    menuItemList.add(item);
                }
                menu.setMenuItems(menuItemList);
            }
            b.setMenu(menu);

        } else {
            b.setName(builder.getName());

            Head head = b.getHead();
            head.setTitle(builder.getHead().getTitle());
            head.setDescription(builder.getHead().getDescription());
            head.setImgUrl(builder.getHead().getImgUrl());
            b.setHead(head);

            Gallery gallery = b.getGallery();
            gallery.setTitle(builder.getGallery().getTitle());
            gallery.setDescription(builder.getGallery().getDescription());
            gallery.setBackground(builder.getGallery().getBackground());
            b.setGallery(gallery);

            List<GalleryItems> galleryItemsList = new ArrayList<GalleryItems>();
            if (builder.getGallery().getGalleryItems().size() > 0) {
                b.getGallery().getGalleryItems().clear();
                for (GalleryItems item : builder.getGallery().getGalleryItems()) {
                    item.setGallery(gallery);
                    galleryItemsList.add(item);
                }
                gallery.getGalleryItems().addAll(galleryItemsList);
            }
            b.setGallery(gallery);

            Menu menu = b.getMenu();
            menu.setTitle(builder.getMenu().getTitle());
            menu.setDescription(builder.getMenu().getDescription());

            List<MenuItem> menuItemList = new ArrayList<MenuItem>();
            if (builder.getMenu().getMenuItems().size() > 0) {
                b.getMenu().getMenuItems().clear();
                for (MenuItem item : builder.getMenu().getMenuItems()) {
                    item.setMenu(menu);
                    menuItemList.add(item);
                }
                menu.getMenuItems().addAll(menuItemList);
            }
            b.setMenu(menu);
        }

        user.setBuilder(b);
        // save builder to db.
        Builder savedBuilder = repository.save(b);
        return savedBuilder;
    }
}
