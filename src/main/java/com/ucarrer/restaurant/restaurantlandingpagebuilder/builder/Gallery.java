package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(columnDefinition = "varchar(512) default ''")
    private String subtitle;

    @Column(columnDefinition = "varchar(512) default ''")
    private String backgroundImgUrl;

    @OneToOne(mappedBy = "gallery")
    @JsonIgnore
    private Builder builder;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.REMOVE)
    private Set<GalleryItem> galleryItems;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Set<GalleryItem> getGalleryItems() {
        return galleryItems;
    }

    public void setGalleryItems(Set<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
    }
}
