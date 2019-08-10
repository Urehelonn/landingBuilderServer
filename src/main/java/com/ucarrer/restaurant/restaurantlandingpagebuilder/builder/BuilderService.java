package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("BuilderService")
public class BuilderService {
    @Autowired
    BuilderRepository repository;

    public Builder getBuilderByUser(User user) {
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
        System.out.println(user.getUsername());
        if (b == null) {
            b = new Builder();
            b.setName(builder.getName());

            Head head = new Head();
            head.setTitle(builder.getHead().getTitle());
            head.setDescription(builder.getHead().getDescription());
            head.setImgUrl(builder.getHead().getImgUrl());
            b.setHead(head);

            Gallery gallery = new Gallery();
            gallery.setTitle(builder.getGallery().getTitle());
            gallery.setDescription(builder.getGallery().getDescription());
            gallery.setBuilder(b);

            if(builder.getGallery().getGalleryItem().size() > 0) {
                List<GalleryItem> galleryItemList = new ArrayList<GalleryItem>();
                for (GalleryItem item : builder.getGallery().getGalleryItem()) {
                    item.setGallery(gallery);
                    galleryItemList.add(item);
                }
                gallery.setGalleryItem(galleryItemList);
            }
            b.setGallery(gallery);

            Menu menu = new Menu();
            menu.setTitle(builder.getMenu().getTitle());
            menu.setDescription(builder.getMenu().getDescription());
            List<MenuItem> menuItemList = new ArrayList<MenuItem>();
            if (builder.getMenu().getMenuItems().size() > 0) {
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
            gallery.setTitle(builder.getHead().getTitle());
            gallery.setDescription(builder.getHead().getDescription());

            List<GalleryItem> galleryItemList = new ArrayList<GalleryItem>();
            if (builder.getGallery().getGalleryItem().size() > 0) {
                b.getGallery().getGalleryItem().clear();
                for (GalleryItem item : builder.getGallery().getGalleryItem()) {
                    item.setGallery(gallery);
                    galleryItemList.add(item);
                }
                gallery.setGalleryItem(galleryItemList);
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
                menu.setMenuItems(menuItemList);
            }
            b.setMenu(menu);
        }

        user.setBuilder(b);
        // save builder to db.
        Builder savedBuilder = repository.save(b);
        return savedBuilder;
    }
}
