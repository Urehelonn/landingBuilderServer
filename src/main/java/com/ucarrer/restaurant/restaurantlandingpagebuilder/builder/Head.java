package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "heads")
public class Head {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "varchar(255) default ''")
    private String title;

    @Column(columnDefinition = "varchar(512) default ''")
    private String subtitle;

    @Column(columnDefinition = "varchar(51) default ''")
    private String logoUrl;



    @Column(columnDefinition = "varchar(512) default ''")
    private String backgroundImgUrl;

    @OneToOne(mappedBy = "head")
    @JsonIgnore
    private Builder builder;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }
}
