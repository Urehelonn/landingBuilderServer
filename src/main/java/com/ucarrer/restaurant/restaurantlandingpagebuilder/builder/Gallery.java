package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "galleries")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255) default ''")
    private String title;

    @Column(columnDefinition = "varchar(1024) default ''")
    private String description;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.REMOVE)
    private Set<GalleryItem> galleryItemSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GalleryItem> getGalleryItemSet() {
        return galleryItemSet;
    }

    public void setGalleryItemSet(Set<GalleryItem> galleryItemSet) {
        this.galleryItemSet = galleryItemSet;
    }
}
